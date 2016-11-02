#!/usr/bin/env python3
"""
Dependencies:
pip3 install beautifulsoup4
"""
import os
import re
import sqlite3
import urllib.request
from bs4 import BeautifulSoup


class TextProcessor:
    def __init__(self, url):
        self.SOURCE_FILE = 'source.html'
        self.OUTPUT_DIR = 'output/'
        self.HTML_OUTPUT_DIR = 'output/html/'
        self.RAW_OUTPUT_DIR = 'output/raw/'
        self.DATABASE_NAME = 'output/data.db'
        self.FILE_LOADED = False

        self.GREEN_TXT = '\033[1;32m'
        self.NORMAL_TXT = '\033[0m'
        self.RED_TEXT = '\033[1;31m'

        if not any(p in url for p in ['http', 'https']):
            url = 'http://' + url

        try:
            print('Downloading file...')
            urllib.request.urlretrieve(url, self.SOURCE_FILE)
            print(self.GREEN_TXT + 'File downloaded' + self.NORMAL_TXT)
            self.FILE_LOADED = True
        except:
            print(self.RED_TEXT + 'Error: ' + self.NORMAL_TXT + "Can't load the url")

        self.verify_dirs()
        self.clear_dirs()
        self.create_database()

    def verify_dirs(self):
        dirs = [self.OUTPUT_DIR, self.HTML_OUTPUT_DIR, self.RAW_OUTPUT_DIR]
        for dir in dirs:
            if not os.path.exists(dir):
                os.makedirs(dir)

    def clear_dirs(self):
        dirs = [self.HTML_OUTPUT_DIR, self.RAW_OUTPUT_DIR]
        for d in dirs:
            filelist = [f for f in os.listdir(d)]
            for f in filelist:
                os.remove(d + f)

    def processor(self):
        if not self.FILE_LOADED:
            return

        temp = []
        counter = 0

        with open(self.SOURCE_FILE, encoding='latin-1') as f:
            # Separating the sections of the text in several different files
            for line in f:
                if '<hr>' in line:
                    if len(temp) != 0:
                        # Ignoring the first section
                        if counter > 0:
                            section_name = str(temp[0]).split('"')[1]
                            file_name = section_name + '.html'
                            path = self.HTML_OUTPUT_DIR + file_name

                            # Stopping point
                            if section_name == 'Concept-Index':
                                break

                            # Writing a new file
                            with open(path, 'wb') as new_file:
                                new_file.writelines(temp)

                            print('File ' + str(counter) + ': ' + file_name)
                            self.file_optimization(path)
                            print('  Optimization\t\t' + self.GREEN_TXT + 'OK' + self.NORMAL_TXT)
                            self.raw_data(path)
                            print('  Raw data\t\t' + self.GREEN_TXT + 'OK' + self.NORMAL_TXT)
                            self.data_persist(file_name)
                            print('  Database\t\t' + self.GREEN_TXT + 'OK' + self.NORMAL_TXT)

                        counter += 1
                        temp = []
                else:
                    temp.append(line.encode('latin-1'))

        print(self.GREEN_TXT + 'Process completed without errors' + self.NORMAL_TXT)

    def file_optimization(self, file_path):
        with open(file_path, encoding='latin-1') as f:
            soup = BeautifulSoup(f, 'html5lib')

            # Creating a charset meta in all files
            meta_tag = soup.new_tag('meta')
            meta_tag['content'] = 'text/html; charset=ISO-8859-1'
            meta_tag['http-equiv'] = 'Content-Type'
            soup.html.head.insert(0, meta_tag)

            # Inserting base style
            base_css = soup.new_tag('link')
            base_css['rel'] = 'stylesheet'
            base_css['href'] = 'css/base.css'
            base_css['type'] = 'text/css'
            soup.head.insert(1, base_css)

            # Inserting font-size style
            font_size_style = soup.new_tag('link')
            font_size_style['rel'] = 'stylesheet'
            font_size_style['href'] = 'css/fontsize-default.css'
            font_size_style['type'] = 'text/css'
            font_size_style['id'] = 'fontsize'
            soup.head.insert(2, font_size_style)

            # Inserting js src
            js_src = soup.new_tag('script')
            js_src['type'] = 'text/javascript'
            js_src['src'] = 'js/script.js'
            soup.head.insert(3, js_src)

            # Initializing js
            js_init = soup.new_tag('script')
            js_init['type'] = 'text/javascript'
            js_init.string = 'configureStyleSheet();'
            soup.head.insert(4, js_init)

            # Replacing <a href="file_name.html#section_name"> to <a href="section_name.html">
            for link_tag in soup.find_all(href=re.compile('#')):
                link_tag['href'] = link_tag['href'][1:] + ".html"

            # Removing the number of section/chapter
            count = 2
            title_tags = [soup.h2, soup.h3, soup.h4]
            for title in title_tags:
                if title:
                    if len(title.contents) == 1:
                        words_list = str(title.string).split(' ')

                        if words_list[0].replace('.', '').isdigit():
                            words_list = words_list[1:]

                        new_string = words_list[0]
                        for w in words_list[1:]:
                            new_string += ' ' + w

                        title.string = new_string
                    else:
                        # Remove only the number of <h2>8.5 <code>foo</code></h2>
                        del(title.contents[0])

                count += 1

            # Removing nav bar
            div_tag = soup.find('div', class_='header')
            if div_tag:
                div_tag.extract()

            # Exporting changes
            html = soup.contents

        html_bytes = []
        for line in html:
            html_bytes.append(line.encode('latin-1'))

        with open(file_path, 'wb') as f:
            f.writelines(html_bytes)

    def raw_data(self, file_path):
        with open(file_path, encoding='latin-1') as f:
            soup = BeautifulSoup(f, 'html5lib')

            # Removing title
            title_tag = [soup.h2, soup.h3, soup.h4]
            for title in title_tag:
                if title:
                    title.extract()

            raw_text = soup.getText()

        raw_text = raw_text.replace('\n', ' ')
        file_name = file_path.split('/')[-1].split('.')[0]
        raw_output_path = self.RAW_OUTPUT_DIR + file_name + '.txt'
        raw_text_b = []

        # Encoding the raw text to ascii and ignoring special characters
        for line in raw_text:
            raw_text_b.append(line.encode('ascii', 'ignore'))

        with open(raw_output_path, 'wb') as f:
            f.writelines(raw_text_b)

    def create_database(self):
        conn = sqlite3.connect(self.DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('DROP TABLE IF EXISTS documentations')
        cursor.execute('''CREATE TABLE documentations (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       file_name TEXT NOT NULL,
                       title TEXT NOT NULL,
                       data TEXT NOT NULL)''')
        conn.commit()
        conn.close()

    def data_persist(self, file_name):
        file_path = self.HTML_OUTPUT_DIR + file_name
        raw_data_path = self.RAW_OUTPUT_DIR + file_name.split('.')[0] + '.txt'
        values = [file_name]

        with open(file_path, encoding='latin-1') as f:
            soup = BeautifulSoup(f, 'html5lib')
            for t in [soup.h2, soup.h3, soup.h4]:
                if t:
                    title = ''
                    for w in t.strings:
                        title += w
                    values.append(title)

        with open(raw_data_path, encoding='ascii') as f:
            data = f.readlines()

        data_str = ''
        for line in data:
            data_str += line
        values.append(data_str)

        conn = sqlite3.connect(self.DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute("INSERT INTO documentations(file_name, title, data) VALUES (?, ?, ?)", values)
        conn.commit()
        conn.close()

    def data_dump(self):
        conn = sqlite3.connect(self.DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT * FROM documentations')
        for row in cursor.fetchall():
            print(row)
        conn.close()

tp = TextProcessor('http://svn.gna.org/viewcvs/*checkout*/latexrefman/trunk/latex2e.html')
tp.processor()

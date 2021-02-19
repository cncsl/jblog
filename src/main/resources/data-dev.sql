INSERT INTO authority (name) VALUES ('ROLE_ADMIN');
INSERT INTO authority (name) VALUES ('ROLE_USER');
INSERT INTO authority (name) VALUES ('ROLE_WRITER');

INSERT INTO user (username, password_hash, display_name, email, activated, activation_key, created_date, reset_key, reset_date) VALUES ('admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', '系统管理员', 'admin@jblog', true, null, '2021-02-18 00:00:00', null, null);
INSERT INTO user (username, password_hash, display_name, email, activated, activation_key, created_date, reset_key, reset_date) VALUES ('user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', '测试用户', 'user@jblog', true, null, '2021-02-18 01:00:00', null, null);

INSERT INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO user_authority (user_id, authority_name) VALUES (2, 'ROLE_USER');
INSERT INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_WRITER');
INSERT INTO user_authority (user_id, authority_name) VALUES (2, 'ROLE_WRITER');

INSERT INTO blog (blog_name, desc_info, user_id) VALUES ('system''s blog', '该博客中的文章内容是 jblog 系统的使用手册', 1);
INSERT INTO blog (blog_name, desc_info, user_id) VALUES ('user''s blog', '这是一个用户博客', 2);

INSERT INTO post (title, abstract_info, content, created_date, modified_date, blog_id) VALUES ('描述', '简述 jblog 用到的技术栈', 'SpringBoot 和 SpringMVC 进行开发，Spring Data JPA 实现数据访问层，Spring Security 和 JWT 进行鉴权，AOP 消除鉴权相关的样板代码', '2021-02-18 20:52:47', '2021-02-18 20:52:49', 1);
INSERT INTO post (title, abstract_info, content, created_date, modified_date, blog_id) VALUES ('todo', '待办事项', '使用 vuejs 写前端', '2021-02-18 20:53:07', '2021-02-18 20:53:09', 1);
INSERT INTO post (title, abstract_info, content, created_date, modified_date, blog_id) VALUES ('Markdown 语法', '总结 markdown 语法', '## Block Elements

### Paragraph and line breaks

A paragraph is simply one or more consecutive lines of text. In markdown source code, paragraphs are separated by two or more blank lines. In Typora, you only need one blank line (press `Return` once) to create a new paragraph.

Press `Shift` + `Return` to create a single line break. Most other markdown parsers will ignore single line breaks, so in order to make other markdown parsers recognize your line break, you can leave two spaces at the end of the line, or insert `<br/>`.

### Headers

Headers use 1-6 hash (`#`) characters at the start of the line, corresponding to header levels 1-6. For example:

``` markdown
# This is an H1

## This is an H2

###### This is an H6
```

In Typora, input ‘#’s followed by title content, and press `Return` key will create a header.

### Blockquotes

Markdown uses email-style > characters for block quoting. They are presented as:

``` markdown
> This is a blockquote with two paragraphs. This is first paragraph.
>
> This is second pragraph. Vestibulum enim wisi, viverra nec, fringilla in, laoreet vitae, risus.



> This is another blockquote with one paragraph. There is three empty line to seperate two blockquote.
```

In Typora, inputting ‘>’ followed by your quote contents will generate a quote block. Typora will insert a proper ‘>’ or line break for you. Nested block quotes (a block quote inside another block quote) by adding additional levels of ‘>’.

### Lists

Input `* list item 1` will create an unordered list - the `*` symbol can be replace with `+` or `-`.

Input `1. list item 1` will create an ordered list - their markdown source code is as follows:

``` markdown
## un-ordered list
*   Red
*   Green
*   Blue

## ordered list
1.  Red
2. 	Green
3.	Blue
```

### Task List

Task lists are lists with items marked as either [ ] or [x] (incomplete or complete). For example:

``` markdown
- [ ] a task list item
- [ ] list syntax required
- [ ] normal **formatting**, @mentions, #1234 refs
- [ ] incomplete
- [x] completed
```

You can change the complete/incomplete state by clicking on the checkbox before the item.

### (Fenced) Code Blocks

Typora only supports fences in GitHub Flavored Markdown. Original code blocks in markdown are not supported.

Using fences is easy: Input \\`\\`\\` and press `return`. Add an optional language identifier after \\`\\`\\` and we''ll run it through syntax highlighting:

``` markdown
Here''s an example:

​```
function test() {
  console.log("notice the blank line before this function?");
}
​```

syntax highlighting:
​```ruby
require ''redcarpet''
markdown = Redcarpet.new("Hello World!")
puts markdown.to_html
​```
```

### Math Blocks

You can render *LaTeX* mathematical expressions using **MathJax**.

To add a mathematical expression, input `$$` and press the ''Return'' key. This will trigger an input field which accepts *Tex/LaTex* source. For example:


$$
\\mathbf{V}_1 \\times \\mathbf{V}_2 =  \\begin{vmatrix}
\\mathbf{i} & \\mathbf{j} & \\mathbf{k} \\\\
\\frac{\\partial X}{\\partial u} &  \\frac{\\partial Y}{\\partial u} & 0 \\\\
\\frac{\\partial X}{\\partial v} &  \\frac{\\partial Y}{\\partial v} & 0 \\\\
\\end{vmatrix}
$$


In the markdown source file, the math block is a *LaTeX* expression wrapped by a pair of ‘$$’ marks:

``` markdown
$$
\\mathbf{V}_1 \\times \\mathbf{V}_2 =  \\begin{vmatrix}
\\mathbf{i} & \\mathbf{j} & \\mathbf{k} \\\\
\\frac{\\partial X}{\\partial u} &  \\frac{\\partial Y}{\\partial u} & 0 \\\\
\\frac{\\partial X}{\\partial v} &  \\frac{\\partial Y}{\\partial v} & 0 \\\\
\\end{vmatrix}
$$
```

You can find more details [here](https://support.typora.io/Math/).

### Tables

Input `| First Header  | Second Header |` and press the `return` key. This will create a table with two columns.

After a table is created, putting focus on that table will open up a toolbar for the table where you can resize, align, or delete the table. You can also use the context menu to copy and add/delete individual columns/rows.

The full syntax for tables is described below, but it is not necessary to know the full syntax in detail as the markdown source code for tables is generated automatically by Typora.

In markdown source code, they look like:

``` markdown
| First Header  | Second Header |
| ------------- | ------------- |
| Content Cell  | Content Cell  |
| Content Cell  | Content Cell  |
```

You can also include inline Markdown such as links, bold, italics, or strikethrough in the table.

Finally, by including colons (`:`) within the header row, you can define text in that column to be left-aligned, right-aligned, or center-aligned:

``` markdown
| Left-Aligned  | Center Aligned  | Right Aligned |
| :------------ |:---------------:| -----:|
| col 3 is      | some wordy text | $1600 |
| col 2 is      | centered        |   $12 |
| zebra stripes | are neat        |    $1 |
```

A colon on the left-most side indicates a left-aligned column; a colon on the right-most side indicates a right-aligned column; a colon on both sides indicates a center-aligned column.

### Footnotes

``` markdown
You can create footnotes like this[^footnote].

[^footnote]: Here is the *text* of the **footnote**.
```

will produce:

You can create footnotes like this[^footnote].

[^footnote]: Here is the *text* of the **footnote**.

Hover over the ‘footnote’ superscript to see content of the footnote.

### Horizontal Rules

Inputting `***` or `---` on a blank line and pressing `return` will draw a horizontal line.

------

### YAML Front Matter

Typora now supports [YAML Front Matter](http://jekyllrb.com/docs/frontmatter/). Input `---` at the top of the article and then press `Return` to introduce a metadata block. Alternatively, you can insert a metadata block from the top menu of Typora.

### Table of Contents (TOC)

Input `[toc]` and press the `Return` key. This will create a  “Table of Contents” section. The TOC extracts all headers from the document, and its contents are updated automatically as you add to the document.', '2021-02-18 20:55:15', '2021-02-18 20:55:16', 2);

INSERT INTO comment (content, relay_to_id, created_date, user_id, post_id) VALUES ('jblog 预计于二月底完成后端主要功能', null, '2021-02-18 21:01:11', 1, 1);
INSERT INTO comment (content, relay_to_id, created_date, user_id, post_id) VALUES ('欢迎 fork 和 PR', null, '2021-02-18 21:01:47', 1, 1);
INSERT INTO comment (content, relay_to_id, created_date, user_id, post_id) VALUES ('摘自 Typora 手册', null, '2021-02-18 21:02:06', 2, 3);
INSERT INTO comment (content, relay_to_id, created_date, user_id, post_id) VALUES ('PR 时请确保新增代码和已有代码保持相同的风格', 2, '2021-02-19 09:12:31', 1, 1);
INSERT INTO comment (content, relay_to_id, created_date, user_id, post_id) VALUES ('Typora 是一款跨平台的 markdown 编辑器', 3, '2021-02-19 09:13:13', 2, 3);
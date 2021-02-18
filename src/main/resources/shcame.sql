create table if not exists authority
(
    name varchar(50) not null
        primary key
)
    comment '权限表';

create table if not exists user
(
    id             bigint auto_increment
        primary key,
    username       varchar(50)  not null comment '用户名',
    password_hash  varchar(60)  not null comment '密码',
    display_name   varchar(50)  null comment '显示名',
    email          varchar(256) null comment '邮箱',
    activated      bit          not null comment '激活标志',
    activation_key varchar(20)  null comment '验证激活校验码',
    created_date   timestamp    null comment '创建时间',
    reset_key      varchar(20)  null comment '重设密码校验码',
    reset_date     timestamp    null comment '重设密码时间',
    constraint ux_user_email
        unique (email),
    constraint ux_user_login
        unique (username)
)
    comment '用户表';

create table if not exists blog
(
    id        bigint auto_increment
        primary key,
    blog_name varchar(20)  not null comment '博客名',
    desc_info varchar(100) null comment '描述信息',
    user_id   bigint       null comment '用户ID',
    constraint fk_blog_user_id
        foreign key (user_id) references user (id)
)
    comment '博客表';

create table if not exists post
(
    id            bigint auto_increment
        primary key,
    title         varchar(20)  not null comment '标题',
    abstract_info varchar(140) null comment '摘要',
    content       longtext     not null comment '正文',
    created_date  datetime     not null comment '创建时间',
    modified_date datetime     not null comment '修改时间',
    blog_id       bigint       null comment '博客ID',
    constraint fk_post_blog_id
        foreign key (blog_id) references blog (id)
)
    comment '文章表';

create table if not exists comments
(
    id           bigint auto_increment
        primary key,
    content      longtext not null comment '内容',
    created_date datetime not null comment '创建时间',
    user_id      bigint   null comment '用户ID',
    post_id      bigint   null comment '文章ID',
    constraint fk_comments_post_id
        foreign key (post_id) references post (id),
    constraint fk_comments_user_id
        foreign key (user_id) references user (id)
)
    comment '评论表';

create table if not exists user_authority
(
    user_id        bigint      not null comment '用户ID',
    authority_name varchar(50) not null comment '权限名',
    primary key (user_id, authority_name),
    constraint fk_authority_name
        foreign key (authority_name) references authority (name),
    constraint fk_user_id
        foreign key (user_id) references user (id)
)
    comment '用户权限表';


create table if not exists m_user
(
	id bigserial not null
		constraint m_user_pk
			primary key,
	login varchar(100) not null,
	password varchar(100) not null,
	role varchar(100) not null,
	created timestamp(6) not null,
	changed timestamp(6) not null
);

alter table m_user owner to postgres;

create unique index if not exists m_user_id_uindex
	on m_user (id);

create unique index if not exists m_user_login_uindex
	on m_user (login);

create table if not exists location
(
	id bigint not null
		constraint location_pk
			primary key,
	location varchar(100) not null
);

alter table location owner to postgres;

create table if not exists movie
(
	id bigint not null
		constraint movie_pk
			primary key,
	title varchar(100) not null,
	genre varchar(100) not null,
	year bigint not null,
	duration bigint not null
);

alter table movie owner to postgres;

create table if not exists cinema
(
	id bigint not null
		constraint cinema_pk
			primary key,
	name varchar(100) not null,
	location_id bigint not null
		constraint cinema_location_id_fk
			references location
				on update cascade on delete cascade,
	movie_id bigint not null
		constraint cinema_movie_id_fk
			references movie (id)
				on update cascade on delete cascade,
	"tickets count" bigint not null,
	"phone number" bigint not null,
	"payment method" varchar(100) not null
);

alter table cinema owner to postgres;

create table if not exists event
(
	id bigint not null
		constraint event_pk
			primary key,
	movie_id bigint not null
		constraint event_movie_id_fk
			references movie
				on update cascade on delete cascade,
	cinema_id bigint not null
		constraint event_cinema_id_fk
			references cinema
				on update cascade on delete cascade,
	"date,time" timestamp(6) not null
);

alter table event owner to postgres;

create table if not exists m_ticket
(
	id bigserial not null
		constraint m_ticket_pk
			primary key,
	user_id bigint not null
		constraint m_ticket_m_user_id_fk
			references m_user
				on update cascade on delete cascade,
	event_id bigint not null
		constraint m_ticket_event_id_fk
			references event
				on update cascade on delete cascade,
	"place number" bigint not null,
	price bigint not null
);

alter table m_ticket owner to postgres;

create unique index if not exists m_ticket_id_uindex
	on m_ticket (id);


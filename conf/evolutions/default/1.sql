# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table categoria (
  c_id                          serial not null,
  d_nombre                      varchar(255) not null,
  constraint uq_categoria_d_nombre unique (d_nombre),
  constraint pk_categoria primary key (c_id)
);

create table producto (
  c_id                          serial not null,
  d_nombre                      varchar(255) not null,
  d_url_foto                    varchar(255),
  d_nombre_categoria            varchar(255),
  f_limite                      varchar(255) not null,
  precio                        integer not null,
  a_ingredientes                varchar(255) not null,
  c_id_sucursal                 integer not null,
  c_id_categoria                integer not null,
  constraint uq_producto_d_nombre unique (d_nombre),
  constraint pk_producto primary key (c_id)
);

create table sucursal (
  c_id                          serial not null,
  d_nombre                      varchar(255) not null,
  a_direccion                   varchar(255) not null,
  constraint uq_sucursal_d_nombre unique (d_nombre),
  constraint pk_sucursal primary key (c_id)
);

alter table producto add constraint fk_producto_c_id_sucursal foreign key (c_id_sucursal) references sucursal (c_id) on delete restrict on update restrict;
create index ix_producto_c_id_sucursal on producto (c_id_sucursal);

alter table producto add constraint fk_producto_c_id_categoria foreign key (c_id_categoria) references categoria (c_id) on delete restrict on update restrict;
create index ix_producto_c_id_categoria on producto (c_id_categoria);


# --- !Downs

alter table if exists producto drop constraint if exists fk_producto_c_id_sucursal;
drop index if exists ix_producto_c_id_sucursal;

alter table if exists producto drop constraint if exists fk_producto_c_id_categoria;
drop index if exists ix_producto_c_id_categoria;

drop table if exists categoria cascade;

drop table if exists producto cascade;

drop table if exists sucursal cascade;


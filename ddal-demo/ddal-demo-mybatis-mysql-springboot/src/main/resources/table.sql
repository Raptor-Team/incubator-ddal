drop table if exists ddal_schema_0.customer;
CREATE TABLE ddal_schema_0.`customer` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `level` tinyint(4) DEFAULT NULL,
  `is_locked` tinyint(1) DEFAULT NULL,
  `last_active_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists ddal_schema_1.customer;
CREATE TABLE ddal_schema_1.`customer` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `level` tinyint(4) DEFAULT NULL,
  `is_locked` tinyint(1) DEFAULT NULL,
  `last_active_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists ddal_schema_2.customer;
CREATE TABLE ddal_schema_2.`customer` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `level` tinyint(4) DEFAULT NULL,
  `is_locked` tinyint(1) DEFAULT NULL,
  `last_active_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists ddal_schema_3.customer;
CREATE TABLE ddal_schema_3.`customer` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `level` tinyint(4) DEFAULT NULL,
  `is_locked` tinyint(1) DEFAULT NULL,
  `last_active_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
create user ddal_schema identified by ddal_schema123
-- 跨用户权限
grant IMP_FULL_DATABASE to ddal_schema;

create user ddal_schema_0 identified by ddal_schema_0_123;
grant connect, resource to ddal_schema_0;
GRANT all PRIVILEGES to ddal_schema_0;
GRANT UNLIMITED TABLESPACE TO ddal_schema_0;

create user ddal_schema_1 identified by ddal_schema_1_123;
grant connect, resource to ddal_schema_1;
GRANT all PRIVILEGES to ddal_schema_1;
GRANT UNLIMITED TABLESPACE TO ddal_schema_1;

create user ddal_schema_2 identified by ddal_schema_2_123;
grant connect, resource to ddal_schema_2;
GRANT all PRIVILEGES to ddal_schema_2;
GRANT UNLIMITED TABLESPACE TO ddal_schema_2;

create user ddal_schema_3 identified by ddal_schema_3_123;
grant connect, resource to ddal_schema_3;
GRANT all PRIVILEGES to ddal_schema_3;
GRANT UNLIMITED TABLESPACE TO ddal_schema_3;

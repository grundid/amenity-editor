DROP INDEX IF EXISTS idx_kv_lower;
DROP INDEX IF EXISTS idx_k_lower;

insert into keyvalues select distinct k,v,count(v) from node_tags group by k,v;
delete from keyvalues where valuecount <= 2;

create index idx_kv_lower on keyvalues (lower(k),lower(v));
create index idx_k_lower on keyvalues (lower(k));

VACUUM FULL;
rename table `$schema$`.`$tableName$_1` to `$schema$`.`$tableName$_$suffix$`$$
create table `$schema$`.`$tableName$_0_new` like `$schema$`.`$tableName$_0`$$
rename table `$schema$`.`$tableName$_0` to `$schema$`.`$tableName$_1`, `$schema$`.`$tableName$_0_new` to `$schema$`.`$tableName$_0`$$
SET FOREIGN_KEY_CHECKS = 0$$
DROP TRIGGER IF EXISTS `tr_autogenerate_id`$$
CREATE TRIGGER `tr_autogenerate_id` BEFORE INSERT ON `wsi_record_0` FOR EACH ROW begin
    if new.id is null  or new.id = ''  or new.id = '0'
    then
        set new.id = uuid_short();
    end if;
end
$$
SET FOREIGN_KEY_CHECKS = 1
$$

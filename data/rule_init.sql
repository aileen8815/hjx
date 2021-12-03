delete TJ_DE_RULE_ITEM;
delete TJ_DE_OPTIONAL_ITEM;
delete TJ_DE_RULE;
delete TJ_CE_CONDITION_ITEM;
delete TJ_CE_CALCULATION_ITEM;
delete TJ_CE_RULE_ITEM;
delete TJ_CE_RULE;
delete TJ_CE_RULE_TYPE;
delete TJ_CE_OPTIONAL_ITEM;

insert into TJ_CE_RULE_TYPE(id, code, typename, businesstype) values(1,'01','押金计费规则','inbound');
insert into TJ_CE_RULE_TYPE(id, code, typename, businesstype) values(2,'02','装卸费计费规则','inbound');
insert into TJ_CE_RULE_TYPE(id, code, typename, businesstype) values(3,'03','分拣费计费规则','inbound');
insert into TJ_CE_RULE_TYPE(id, code, typename, businesstype) values(4,'04','装卸费计费规则','outbound');
insert into TJ_CE_RULE_TYPE(id, code, typename, businesstype) values(5,'05','仓储费计费规则','outbound');
insert into TJ_CE_RULE_TYPE(id, code, typename, businesstype) values(6,'06','运输费规则','outbound');
insert into TJ_CE_RULE_TYPE(id, code, typename, businesstype) values(7,'07','押金计费规则','ownerchangeforbuyer');
insert into TJ_CE_RULE_TYPE(id, code, typename, businesstype) values(8,'08','仓储费计费规则','ownerchangeforseller');
insert into TJ_CE_RULE_TYPE(id, code, typename, businesstype) values(9,'09','月结仓储费规则','monthknot');

--入库费用规则
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(1,'PalletAmount', '托盘数', 'rule', 'entity', 'digit', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(2,'PalletAmount', '托盘数', 'calculation', 'entity', 'digit', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(3,'PalletAmount', '托盘数', 'condition', 'entity', 'digit', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(4,'Customer', '客户', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(5,'Customer', '客户', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(6,'Product', '存储商品', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Product','/combo-refer/product','name','inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(7,'Product', '存储商品', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.Product','/combo-refer/product','name','inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(8,'ProductCategory', '商品类型', 'rule', 'getProduct', 'hierarchy', 'com.wwyl.entity.settings.ProductCategory','/combo-refer/product-category','name','inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(9,'ProductCategory', '商品类型', 'condition', 'getProduct', 'hierarchy', 'com.wwyl.entity.settings.ProductCategory','/combo-refer/product-category','name','inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(10,'ReceiptTime', '入库时间', 'rule', 'entity', 'date', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(11,'ReceiptTime', '入库时间', 'condition', 'entity', 'date', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(12,'PalletDayAmount', '托盘*天数', 'rule', 'entity', 'digit', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(13,'PalletDayAmount', '托盘*天数', 'calculation', 'entity', 'digit', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(14,'PalletDayAmount', '托盘*天数', 'condition', 'entity', 'digit', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(15,'WeightAmount', '重量', 'rule', 'entity', 'digit', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(16,'WeightAmount', '重量', 'calculation', 'entity', 'digit', null,null,null,'inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(17,'WeightAmount', '重量', 'condition', 'entity', 'digit', null,null,null,'inbound');

insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(72,'Product', '商品(参考重量)', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Product',null,'weight','inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(73,'Product', '商品(参考重量)', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.Product',null,'weight','inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(74,'Product', '商品(参考重量)', 'calculation', 'entity', 'digit', 'com.wwyl.entity.settings.Product',null,'weight','inbound');

insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(75,'CustomerGrade', '客户等级', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','inbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(76,'CustomerGrade', '客户等级', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','inbound');


--出库费用规则
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(18,'PalletAmount', '托盘数', 'rule', 'entity', 'digit', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(19,'PalletAmount', '托盘数', 'calculation', 'entity', 'digit', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(20,'PalletAmount', '托盘数', 'condition', 'entity', 'digit', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(21,'Customer', '客户', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(22,'Customer', '客户', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(23,'Product', '存储商品', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Product','/combo-refer/product','name','outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(24,'Product', '存储商品', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.Product','/combo-refer/product','name','outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(25,'ProductCategory', '商品类型', 'rule', 'getProduct', 'hierarchy', 'com.wwyl.entity.settings.ProductCategory','/combo-refer/product-category','name','outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(26,'ProductCategory', '商品类型', 'condition', 'getProduct', 'hierarchy', 'com.wwyl.entity.settings.ProductCategory','/combo-refer/product-category','name','outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(27,'ReceiptTime', '入库时间', 'rule', 'entity', 'date', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(28,'ReceiptTime', '入库时间', 'condition', 'entity', 'date', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(29,'PalletDayAmount', '托盘*天数', 'rule', 'entity', 'digit', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(30,'PalletDayAmount', '托盘*天数', 'calculation', 'entity', 'digit', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(31,'PalletDayAmount', '托盘*天数', 'condition', 'entity', 'digit', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(32,'WeightAmount', '重量', 'rule', 'entity', 'digit', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(33,'WeightAmount', '重量', 'calculation', 'entity', 'digit', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(34,'WeightAmount', '重量', 'condition', 'entity', 'digit', null,null,null,'outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(35,'CustomerGrade', '客户等级', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','outbound');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(36,'CustomerGrade', '客户等级', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','outbound');


--货权转移买方规则
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(37,'PalletAmount', '托盘数', 'rule', 'entity', 'digit', null,null,null,'ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(38,'PalletAmount', '托盘数', 'calculation', 'entity', 'digit', null,null,null,'ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(39,'PalletAmount', '托盘数', 'condition', 'entity', 'digit', null,null,null,'ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(40,'PalletDayAmount', '托盘*天数', 'rule', 'entity', 'digit', null,null,null,'ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(41,'PalletDayAmount', '托盘*天数', 'calculation', 'entity', 'digit', null,null,null,'ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(42,'PalletDayAmount', '托盘*天数', 'condition', 'entity', 'digit', null,null,null,'ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(43,'Customer', '客户', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(44,'Customer', '客户', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(45,'Product', '存储商品', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Product','/combo-refer/product','name','ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(46,'Product', '存储商品', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.Product','/combo-refer/product','name','ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(47,'ProductCategory', '商品类型', 'rule', 'getProduct', 'hierarchy', 'com.wwyl.entity.settings.ProductCategory','/combo-refer/product-category','name','ownerchangeforbuyer');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(48,'ProductCategory', '商品类型', 'condition', 'getProduct', 'hierarchy', 'com.wwyl.entity.settings.ProductCategory','/combo-refer/product-category','name','ownerchangeforbuyer');

--货权转移卖方规则
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(49,'PalletAmount', '托盘数', 'rule', 'entity', 'digit', null,null,null,'ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(50,'PalletAmount', '托盘数', 'calculation', 'entity', 'digit', null,null,null,'ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(51,'PalletAmount', '托盘数', 'condition', 'entity', 'digit', null,null,null,'ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(52,'PalletDayAmount', '托盘*天数', 'rule', 'entity', 'digit', null,null,null,'ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(53,'PalletDayAmount', '托盘*天数', 'calculation', 'entity', 'digit', null,null,null,'ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(54,'PalletDayAmount', '托盘*天数', 'condition', 'entity', 'digit', null,null,null,'ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(55,'Customer', '客户', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(56,'Customer', '客户', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(57,'Product', '存储商品', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Product','/combo-refer/product','name','ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(58,'Product', '存储商品', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.Product','/combo-refer/product','name','ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(59,'ProductCategory', '商品类型', 'rule', 'getProduct', 'hierarchy', 'com.wwyl.entity.settings.ProductCategory','/combo-refer/product-category','name','ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(60,'ProductCategory', '商品类型', 'condition', 'getProduct', 'hierarchy', 'com.wwyl.entity.settings.ProductCategory','/combo-refer/product-category','name','ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(61,'CustomerGrade', '客户等级', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(62,'CustomerGrade', '客户等级', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(63,'WeightAmount', '重量', 'rule', 'entity', 'digit', null,null,null,'ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(64,'WeightAmount', '重量', 'calculation', 'entity', 'digit', null,null,null,'ownerchangeforseller');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(65,'WeightAmount', '重量', 'condition', 'entity', 'digit', null,null,null,'ownerchangeforseller');

--月结仓储费计费
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(66,'PalletDayAmount', '托盘*天数', 'rule', 'entity', 'digit', null,null,null,'monthknot');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(67,'PalletDayAmount', '托盘*天数', 'calculation', 'entity', 'digit', null,null,null,'monthknot');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(68,'PalletDayAmount', '托盘*天数', 'condition', 'entity', 'digit', null,null,null,'monthknot');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(69,'WeightAmount', '重量', 'rule', 'entity', 'digit', null,null,null,'monthknot');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(70,'WeightAmount', '重量', 'calculation', 'entity', 'digit', null,null,null,'monthknot');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(71,'WeightAmount', '重量', 'condition', 'entity', 'digit', null,null,null,'monthknot');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(77,'CustomerGrade', '客户等级', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','monthknot');
insert into TJ_CE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, businesstype)
values(78,'CustomerGrade', '客户等级', 'condition', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','monthknot');

--折扣规则
--入库折扣规则
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(1,'CustomerGrade', '客户等级', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','inbound');
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(2,'Customer', '客户', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','inbound');
--出库折扣规则
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(3,'CustomerGrade', '客户等级', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','outbound');
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(4,'Customer', '客户', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','outbound');
--货权转移买方折扣规则
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(5,'CustomerGrade', '客户等级', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','ownerchangeforbuyer');
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(6,'Customer', '客户', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','ownerchangeforbuyer');
--货权转移卖方折扣规则
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(7,'CustomerGrade', '客户等级', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','ownerchangeforseller');
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(8,'Customer', '客户', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','ownerchangeforseller');
--月结折扣规则
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(9,'CustomerGrade', '客户等级', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.CustomerGrade','/combo-refer/customer-grade','name','monthknot');
insert into TJ_DE_OPTIONAL_ITEM(id, itemname, itemtitle, itemtype, hostname, valuetype, refentity,refsource,refname, discounttype)
values(10,'Customer', '客户', 'rule', 'entity', 'digit', 'com.wwyl.entity.settings.Customer','/combo-refer/customer','name','monthknot');

commit;



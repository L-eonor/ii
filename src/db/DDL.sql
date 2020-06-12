CREATE TABLE `up201504515`.`Order_` ( 
     `ID` SERIAL NOT NULL AUTO_INCREMENT COMMENT 'identificador da Ordem' ,
     `priority` INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'prioridade de execucao' ,
     `t_order_in` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'quando é que a ordem chegou' ,
     `t_start` INT NULL COMMENT 'início de processamento da ordem' ,
     `t_end` INT NULL COMMENT 'término do processamento' ,
     `status` INT NOT NULL COMMENT 'indica se a order está: waiting, processing, done' ,
     manager_ID BIGINT(20) UNSIGNED,
     PRIMARY KEY (`ID`)
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Transformation_Order` (
     order_ID SERIAL NOT NULL ,
     Px INT NOT NULL COMMENT 'qual a peça de origem' ,
     Py INT NOT NULL COMMENT 'tipo de peça final' ,
     n_total INT NOT NULL COMMENT 'número total de peças Py a produzir, equivale a XX' ,
     n_done INT NULL DEFAULT '0' COMMENT 'total de peças completadas' ,
     n_processing INT NULL DEFAULT '0' COMMENT 'número de peças em processamento ' ,
     max_delay INT NOT NULL DEFAULT '0' COMMENT 'máximo delay permitido para a conclusão da ordem de fabrico, equivale a         
          YY' ,
     path TEXT NOT NULL COMMENT ' qual o path que as peças devem tomar para a transformação; este path deve ser o transmitido à celula respetiva' ,
     FOREIGN KEY (order_ID) REFERENCES Order_(ID),
     PRIMARY KEY (order_ID)
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Unload_Units` (
     order_ID SERIAL NOT NULL ,
     Px INT NOT NULL COMMENT 'qual a peça de origem' ,
     max_delay INT NOT NULL DEFAULT '0' COMMENT 'máximo delay permitido para a conclusão da ordem de fabrico, equivale a  YY' ,
     Dy INT NOT NULL COMMENT 'qual o tapete de descarga para o qual enviar a ordem' ,
     path TEXT NOT NULL COMMENT ' qual o path que as peças devem tomar para a transformação; este path deve ser o transmitido à celula respetiva' ,
     FOREIGN KEY (order_ID) REFERENCES Order_(ID),
     PRIMARY KEY (order_ID)
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Load_Units` (
     order_ID SERIAL NOT NULL ,
     Px INT NOT NULL COMMENT 'qual a peça de origem' ,
     max_delay INT NOT NULL DEFAULT '0' COMMENT 'máximo delay permitido para a conclusão da ordem de fabrico, equivale a  YY' ,
     path TEXT NOT NULL COMMENT ' qual o path que as peças devem tomar para a transformação; este path deve ser o transmitido à celula respetiva' ,
     FOREIGN KEY (order_ID) REFERENCES Order_(ID),
     PRIMARY KEY (order_ID)
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Status_Stores` (
     order_ID SERIAL NOT NULL ,
     path TEXT NOT NULL COMMENT 'informação a enviar ' ,
     FOREIGN KEY (order_ID) REFERENCES Order_(ID),
     PRIMARY KEY (order_ID)
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Order_Manager` (
     manager_ID SERIAL NOT NULL ,
     OPC_ID BIGINT(20) UNSIGNED,
     PRIMARY KEY (manager_ID)
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Calculate_Path` (
     calculator_ID SERIAL NOT NULL ,
     DB_ID BIGINT(20) UNSIGNED,
     PRIMARY KEY (calculator_ID)
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`DB_Handler` (
     DB_ID SERIAL NOT NULL PRIMARY KEY,
     user TEXT NOT NULL, 
     passw TEXT NOT NULL,
     manager_ID BIGINT(20) UNSIGNED,
     FOREIGN KEY (manager_ID ) REFERENCES `up201504515`.`Order_Manager`(manager_ID )
       
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`UDP_Handler` (
     UDP_ID SERIAL NOT NULL AUTO_INCREMENT ,
     socket TEXT NOT NULL, 
     port TEXT NOT NULL,
     server_addr TEXT NOT NULL,
     manager_ID BIGINT(20) UNSIGNED,
     FOREIGN KEY ( manager_ID ) REFERENCES `up201504515`.`Order_Manager`(manager_ID ),
     PRIMARY KEY (UDP_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Monitor_State` (
     monitor_ID SERIAL NOT NULL AUTO_INCREMENT ,
     DB_ID BIGINT(20) UNSIGNED, 
     FOREIGN KEY (DB_ID)  REFERENCES `up201504515`.`DB_Handler` (DB_ID),
     PRIMARY KEY (monitor_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`OPC_UA_Handler` (
     OPC_ID SERIAL NOT NULL AUTO_INCREMENT ,
     monitor_ID BIGINT(20) UNSIGNED, 
     FOREIGN KEY (monitor_ID ) REFERENCES `up201504515`.`Monitor_State`(monitor_ID ),
     PRIMARY KEY (OPC_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Cell` (
     cell_ID SERIAL NOT NULL AUTO_INCREMENT ,
     cell_actuators TEXT NOT NULL, 
     OPC_ID BIGINT(20) UNSIGNED,
     FOREIGN KEY (OPC_ID) REFERENCES `up201504515`.`OPC_UA_Handler` (OPC_ID ),
     PRIMARY KEY (cell_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`WareHouse` (
     cell_ID SERIAL NOT NULL ,
     P1 INT NOT NULL,
     P2 INT NOT NULL,
     P3 INT NOT NULL,
     P4 INT NOT NULL,
     P5 INT NOT NULL,
     P6 INT NOT NULL,
     P7 INT NOT NULL,
     P8 INT NOT NULL,
     P9 INT NOT NULL,
     FOREIGN KEY (cell_ID ) REFERENCES Cell(cell_ID ),
     PRIMARY KEY (cell_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Transformation` (
     cell_ID SERIAL NOT NULL ,
     FOREIGN KEY (cell_ID ) REFERENCES Cell(cell_ID ),
     PRIMARY KEY (cell_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Unload_Cell` (
     cell_ID SERIAL NOT NULL ,
     FOREIGN KEY (cell_ID ) REFERENCES Cell(cell_ID ),
     PRIMARY KEY (cell_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Load_Cell` (
     cell_ID SERIAL NOT NULL ,
     FOREIGN KEY (cell_ID ) REFERENCES Cell(cell_ID ),
     PRIMARY KEY (cell_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Unit` (
     unit_ID SERIAL NOT NULL ,
     tipo INT NOT NULL,
     estado TEXT NOT NULL,
     position TEXT NOT NULL, 
     path_to_follow TEXT NULL,
     cell_ID BIGINT(20) UNSIGNED, 
     FOREIGN KEY (cell_ID ) REFERENCES Cell(cell_ID ),
     PRIMARY KEY (unit_ID)
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Actuator` (
     actuator_ID SERIAL NOT NULL ,
     status TEXT NOT NULL,
     cell_ID BIGINT(20) UNSIGNED, 
     FOREIGN KEY (cell_ID ) REFERENCES Cell(cell_ID ),
     PRIMARY KEY (actuator_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Conveyor` (
     actuator_ID SERIAL NOT NULL ,
     orientation TEXT NOT NULL,
     conveyor_queue TEXT NOT NULL,
     FOREIGN KEY (actuator_ID ) REFERENCES Actuator(actuator_ID ),
     PRIMARY KEY (actuator_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Rotator` (
     actuator_ID SERIAL NOT NULL ,
     orientation TEXT NOT NULL,
     rotator_queue TEXT NOT NULL,
     FOREIGN KEY (actuator_ID ) REFERENCES Actuator(actuator_ID ),
     PRIMARY KEY (actuator_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Pusher` (
     actuator_ID SERIAL NOT NULL ,
     pusher_queue TEXT NOT NULL,
     FOREIGN KEY (actuator_ID ) REFERENCES Actuator(actuator_ID ),
     PRIMARY KEY (actuator_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Slider` (
     actuator_ID SERIAL NOT NULL ,
     FOREIGN KEY (actuator_ID ) REFERENCES Actuator(actuator_ID ),
     PRIMARY KEY (actuator_ID )
) ENGINE = InnoDB;


CREATE TABLE `up201504515`.`Machine` (
     actuator_ID SERIAL NOT NULL ,
     type CHAR NOT NULL,
     current_tool TEXT NULL,
     machine_queue TEXT NOT NULL,
     FOREIGN KEY (actuator_ID ) REFERENCES Actuator(actuator_ID ),
     PRIMARY KEY (actuator_ID )
) ENGINE = InnoDB;


alter table `up201504515`.`Order_`
    add constraint fk_order_manager
    foreign key (manager_ID) 
    REFERENCES Order_Manager(manager_ID);

alter table `up201504515`.`Order_Manager`
    add constraint fk_opc_ua_manager
    foreign key (OPC_ID ) 
    REFERENCES OPC_UA_Handler(OPC_ID );

alter table `up201504515`.`Calculate_Path`
    add constraint fk_db_handler
    foreign key (DB_ID) 
    REFERENCES DB_Handler(DB_ID);

INSERT INTO T03_MODULE (T03_ID ,T03_COMMENT ,T03_NAME ) VALUES (1, 'Modulo administratuvo de usuarios', 'USER_MANAGEMENT');
INSERT INTO T02_ROLE  (T02_ID ,T02_NAME ,T02_COMMENT ) VALUES (1, 'admin', 'Rol Administrador');
INSERT INTO T02_ROLE  (T02_ID ,T02_NAME ,T02_COMMENT ) VALUES (2, 'user', 'Rol Usuario normal');
INSERT INTO T02_ROLE_MODULES (ROLE_T02_ID ,MODULES_T03_ID ) VALUES(1, 1);
INSERT INTO T01_USER (T01_ID , T01_EMAIL , T01_LAST_NAMES , T01_NAMES , T01_PASSWORD , T01_USERNAME , ROLE_T02_ID ) VALUES(1, 'root@root.com', 'root', 'root', '$2a$10$N8vg7trqRbryXvHJ.V4sjeU1f6FRHBxbw1fxnw9YjN8SWKWRSIMiy','root', 1);
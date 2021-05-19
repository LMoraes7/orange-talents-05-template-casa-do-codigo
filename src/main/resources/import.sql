insert into autor(nome, email, descricao, instante_cadastrado) values ('Diego', 'diego@email.com', 'Descrição qualquer', '2020-12-25');
insert into autor(nome, email, descricao, instante_cadastrado) values ('Carlos', 'carlos@email.com', 'Descrição qualquer', '2020-12-25');

insert into categoria(nome) values ('Política');
insert into categoria(nome) values ('Infantil');
insert into categoria(nome) values ('Culinária');
insert into categoria(nome) values ('Programação');

insert into livro(titulo, resumo, sumario, preco, paginas, identificador, data_publicacao, categoria_id, autor_id) values ('Spring Boot', 'Livro sobre Spring Boot', 'Sumário Spring Boot qualquer', 50.00, 200, 'sdfgre', '2021-08-15', 4, 1);
insert into livro(titulo, resumo, sumario, preco, paginas, identificador, data_publicacao, categoria_id, autor_id) values ('Java', 'Livro sobre Java', 'Sumário Java qualquer', 150.00, 200, 'fdjkmn', '2021-07-23', 4, 2);
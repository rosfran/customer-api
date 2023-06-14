-- Customers
INSERT INTO public.customer (id, first_name, last_name, age)
VALUES (1, 'Julia', 'van Daan', 32);

INSERT INTO public.customer (id, first_name, last_name, age)
VALUES (2, 'Lars', 'von Trier', 48);

INSERT INTO public.customer (id, first_name, last_name, age)
VALUES (3, 'Sem', 'Japhe', 15);

INSERT INTO public.customer (id, first_name, last_name, age)
VALUES (4, 'Eva', 'van Dirk', 89);

-- Address

INSERT INTO public.address (  id, street_name, number, city, country, customer_id )
VALUES (1, 'Top Naefflaan', 28, 'Amstelveen', 'Netherlands', 1);

INSERT INTO public.address (  id, street_name, number, city, country, customer_id )
VALUES (2, 'Postjesweg', 121, 'Amsterdam', 'Netherlands', 2);

INSERT INTO public.address (  id, street_name, number, city, country, customer_id )
VALUES (3, 'Ernstraat', 3, 'Amsterdam', 'Netherlands', 3);

INSERT INTO public.address (  id, street_name, number, city, country, customer_id )
VALUES (4, 'De Heldinnenlaan', 8, 'Utrecht', 'Netherlands', 4);

SELECT setval('sequence_customer', max(id)) FROM public.customer;

SELECT setval('sequence_address', max(id)) FROM public.address;


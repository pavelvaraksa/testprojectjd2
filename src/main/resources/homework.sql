/*
1) Вывести все уникальные имена ролей пользователей.
*/
select distinct role_name from m_roles join m_users on m_users.id = m_roles.user_id;

/*
2) Подсчитать число машин у каждого пользователя.
Вывести в формате User full name (username + пробел + user surname)
| Число машин у пользователя.
*/

select username,surname,count(model) from m_users join m_cars
on m_users.id = m_cars.user_id group by username,surname;

/*
3) Подсчитать для каждого диллера число машин,
старше 2018 года производства с красным кузовом.
*/

select name,address,count(model) from m_auto_dealer join m_cars on m_auto_dealer.id = m_cars.dealer_id
where year < 2015
group by name,address;

/*
4) Найти пользователей не из Беларуси и России,
у которых есть машина 2010-2015 года выпуска из Германии,
купленная у диллера не в Германии,
с объемом двигателя больше 3 литров.
*/

select username,surname from m_users,m_cars,m_auto_dealer,m_engine
where m_users.country not in('BELARUS','RUSSIA')
and m_cars.year between 2010 and 2015
and m_cars.country in('GERMANY')
and m_auto_dealer.country not in('GERMANY')
and m_engine.volume > 3.0
group by username, surname;

/*
5) Определить логины пользователей, имеющих больше 3 машин.
*/

select login from m_users join m_cars
on m_users.id = m_cars.user_id group by login having count(model) > 3;

/*
6) Вывести уникальных диллеров с подсчитанной суммой стоимостей машин, связанных с ними.
*/

select name,sum(price) from m_auto_dealer join m_cars
on m_auto_dealer.id = m_cars.dealer_id group by name;

/*
7) Подсчитать количество уникальных пользователей,
владеющих хотя бы одной машиной,
стоимость которой превышает среднюю стоимость всех машин.
*/
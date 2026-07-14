-- =========================================================
-- Test data for menu.menu_items + menu.menu_item_ingredients
-- Intended for use with testcontainers (Postgres)
-- Assumes menu.categories and menu.ingredients are already seeded
-- (see the provided reference INSERT scripts)
-- =========================================================

INSERT INTO menu.menu_items
(id, name, description, is_active, price, category_id, cook_time_minutes, weight_grams, image_url)
VALUES
-- Breakfast (1)
(1, 'Scrambled Eggs with Bacon', 'Fluffy scrambled eggs served with crispy fried bacon', TRUE, 350.00, 1, 10, 220.00,
 NULL),
(2, 'Oatmeal with Banana and Honey', 'Warm oats topped with sliced banana and a drizzle of honey', TRUE, 280.00, 1, 12,
 300.00, NULL),

-- Business Lunch (2)
(3, 'Chicken Breast with Rice and Broccoli', 'Grilled chicken breast served with steamed rice and broccoli', TRUE,
 650.00, 2, 25, 400.00, NULL),

-- Seasonal Dishes (3)
(4, 'Zucchini and Tomato Gratin', 'Baked zucchini and tomato layered with melted mozzarella', TRUE, 480.00, 3, 35,
 280.00, NULL),

-- Soups (4)
(5, 'Classic Chicken Soup', 'Hearty chicken soup with carrot, onion and potato', TRUE, 380.00, 4, 40, 350.00, NULL),
(6, 'Creamy Mushroom Soup', 'Velvety mushroom soup finished with heavy cream', TRUE, 420.00, 4, 30, 320.00, NULL),

-- Salads (5)
(7, 'Cucumber Tomato Salad', 'Fresh cucumber and tomato tossed in olive oil dressing', TRUE, 320.00, 5, 10, 250.00,
 NULL),
(8, 'Chicken and Spinach Salad', 'Grilled chicken breast over spinach with shaved parmesan', TRUE, 450.00, 5, 15,
 280.00, NULL),

-- Appetizers (6)
(9, 'Tomato Basil Bruschetta', 'Toasted bread topped with tomato, garlic and basil', TRUE, 300.00, 6, 15, 180.00, NULL),
(10, 'Shrimp Cocktail', 'Chilled shrimp served with lemon and olive oil dressing', TRUE, 520.00, 6, 15, 150.00, NULL),

-- Main Courses (7)
(11, 'Beef and Mushroom Stew', 'Tender beef sirloin braised with mushroom and onion in cream sauce', TRUE, 890.00, 7,
 45, 380.00, NULL),
(12, 'Chicken Thigh with Vegetables', 'Braised chicken thigh with bell pepper, zucchini and onion', TRUE, 720.00, 7, 35,
 400.00, NULL),

-- Grilled Dishes (8)
(13, 'Grilled Salmon Fillet', 'Salmon fillet grilled with lemon and olive oil', TRUE, 980.00, 8, 20, 260.00, NULL),
(14, 'Grilled Lamb Chop', 'Lamb chop seasoned with garlic and black pepper, grilled to order', TRUE, 1200.00, 8, 25,
 280.00, NULL),

-- Pasta & Risotto (9)
(15, 'Pasta Carbonara', 'Pasta with bacon, egg and parmesan in a creamy sauce', TRUE, 620.00, 9, 20, 320.00, NULL),
(16, 'Wild Mushroom Risotto', 'Creamy rice risotto with mushroom, parmesan and butter', TRUE, 680.00, 9, 30, 320.00,
 NULL),

-- Pizza (10)
(17, 'Margherita Pizza', 'Classic pizza with tomato, mozzarella and basil', TRUE, 550.00, 10, 18, 450.00, NULL),
(18, 'Bacon and Cheese Pizza', 'Pizza topped with bacon, tomato sauce and mozzarella', TRUE, 620.00, 10, 18, 470.00,
 NULL),

-- Fish & Seafood (11)
(19, 'Seared Tuna Steak', 'Tuna steak seared and finished with lemon and olive oil', TRUE, 950.00, 11, 15, 250.00,
 NULL),
(20, 'Garlic Butter Mussels', 'Mussels sauteed in garlic butter with a splash of lemon', TRUE, 780.00, 11, 20, 300.00,
 NULL),

-- Meat Dishes (12)
(21, 'Grilled Beef Sirloin Steak', 'Beef sirloin steak with black pepper and olive oil', TRUE, 1100.00, 12, 25, 300.00,
 NULL),
(22, 'Duck Breast with Orange Glaze', 'Pan-seared duck breast finished with an orange honey glaze', TRUE, 1050.00, 12,
 30, 280.00, NULL),

-- Side Dishes (13)
(23, 'Creamy Mashed Potato', 'Mashed potato with butter and milk', TRUE, 220.00, 13, 20, 220.00, NULL),
(24, 'Grilled Seasonal Vegetables', 'Zucchini, bell pepper and broccoli grilled with olive oil', TRUE, 260.00, 13, 15,
 220.00, NULL),

-- Desserts (14)
(25, 'Classic Apple Pie', 'Baked pie with spiced apple filling', TRUE, 380.00, 14, 60, 160.00, NULL),
(26, 'Greek Yogurt with Honey and Walnuts', 'Greek yogurt topped with honey, walnuts and strawberry', TRUE, 340.00, 14,
 5, 200.00, NULL),

-- Bakery (15)
(27, 'Fresh Baked Bread', 'Traditional oven-baked bread loaf', TRUE, 180.00, 15, 60, 350.00, NULL),
(28, 'Almond Croissant', 'Buttery croissant filled with almond cream', TRUE, 260.00, 15, 45, 90.00, NULL),

-- Kids Menu (16)
(29, 'Crispy Chicken Nuggets', 'Breaded chicken breast nuggets, kid-friendly portion', TRUE, 350.00, 16, 15, 180.00,
 NULL),

-- Vegan Menu (17)
(30, 'Chickpea and Vegetable Salad', 'Chickpeas with tomato, cucumber, olive oil and lemon', TRUE, 380.00, 17, 15,
 260.00, NULL),
(31, 'Lentil Soup', 'Hearty lentil soup with carrot, onion and garlic', TRUE, 350.00, 17, 30, 320.00, NULL),

-- Beverages (18)
(32, 'Fresh Orange Juice', 'Freshly squeezed orange juice', TRUE, 220.00, 18, 5, 300.00, NULL),
(33, 'Homemade Lemonade', 'Lemonade sweetened with sugar and honey', TRUE, 200.00, 18, 5, 300.00, NULL),

-- Coffee & Tea (19)
(34, 'Cappuccino', 'Espresso with steamed milk foam', TRUE, 260.00, 19, 5, 200.00, NULL),

-- Cocktails (20)
(35, 'Virgin Lemon Fizz', 'Non-alcoholic lemon and sugar cocktail', TRUE, 300.00, 20, 5, 250.00, NULL),

-- Alcoholic Beverages (21)
(36, 'House Red Wine Glass', 'A glass of the house red wine', TRUE, 420.00, 21, 2, 150.00, NULL),

-- Wines (22)
(37, 'Chardonnay', 'Dry white wine, bottle', TRUE, 2200.00, 22, 2, 750.00, NULL),

-- Specials (23)
(38, 'Chef''s Salmon Special', 'Salmon fillet with avocado, lemon and olive oil', TRUE, 1050.00, 23, 25, 300.00, NULL);

SELECT setval('menu.menu_items_id_seq', (SELECT MAX(id) FROM menu.menu_items));

-- =========================================================
-- menu.menu_item_ingredients
-- =========================================================

INSERT INTO menu.menu_item_ingredients (menu_item_id, ingredient_id, weight_grams)
VALUES
-- 1 Scrambled Eggs with Bacon: egg(14), bacon(6), butter(16)
(1, 14, 120.00),
(1, 6, 80.00),
(1, 16, 20.00),

-- 2 Oatmeal with Banana and Honey: oats(29), banana(43), honey(55), milk(15)
(2, 29, 80.00),
(2, 43, 100.00),
(2, 55, 20.00),
(2, 15, 100.00),

-- 3 Chicken Breast with Rice and Broccoli: chicken breast(1), rice(23), broccoli(36)
(3, 1, 200.00),
(3, 23, 150.00),
(3, 36, 50.00),

-- 4 Zucchini and Tomato Gratin: zucchini(40), tomato(31), mozzarella(18), olive oil(51)
(4, 40, 120.00),
(4, 31, 100.00),
(4, 18, 50.00),
(4, 51, 10.00),

-- 5 Classic Chicken Soup: chicken breast(1), carrot(34), onion(32), potato(27)
(5, 1, 120.00),
(5, 34, 50.00),
(5, 32, 40.00),
(5, 27, 100.00),

-- 6 Creamy Mushroom Soup: mushroom(38), heavy cream(21), onion(32), butter(16)
(6, 38, 150.00),
(6, 21, 80.00),
(6, 32, 40.00),
(6, 16, 20.00),

-- 7 Cucumber Tomato Salad: cucumber(39), tomato(31), onion(32), olive oil(51)
(7, 39, 100.00),
(7, 31, 100.00),
(7, 32, 30.00),
(7, 51, 15.00),

-- 8 Chicken and Spinach Salad: chicken breast(1), spinach(37), parmesan(19)
(8, 1, 150.00),
(8, 37, 80.00),
(8, 19, 30.00),

-- 9 Tomato Basil Bruschetta: bread(30), tomato(31), garlic(33), basil(58), olive oil(51)
(9, 30, 80.00),
(9, 31, 60.00),
(9, 33, 5.00),
(9, 58, 5.00),
(9, 51, 10.00),

-- 10 Shrimp Cocktail: shrimp(11), lemon(44), olive oil(51)
(10, 11, 120.00),
(10, 44, 15.00),
(10, 51, 10.00),

-- 11 Beef and Mushroom Stew: beef sirloin(3), mushroom(38), onion(32), heavy cream(21)
(11, 3, 200.00),
(11, 38, 100.00),
(11, 32, 40.00),
(11, 21, 50.00),

-- 12 Chicken Thigh with Vegetables: chicken thigh(2), bell pepper(35), zucchini(40), onion(32)
(12, 2, 200.00),
(12, 35, 80.00),
(12, 40, 80.00),
(12, 32, 40.00),

-- 13 Grilled Salmon Fillet: salmon(9), lemon(44), olive oil(51)
(13, 9, 220.00),
(13, 44, 15.00),
(13, 51, 10.00),

-- 14 Grilled Lamb Chop: lamb chop(8), garlic(33), black pepper(57)
(14, 8, 250.00),
(14, 33, 5.00),
(14, 57, 3.00),

-- 15 Pasta Carbonara: pasta(25), bacon(6), egg(14), parmesan(19), black pepper(57)
(15, 25, 150.00),
(15, 6, 60.00),
(15, 14, 60.00),
(15, 19, 30.00),
(15, 57, 2.00),

-- 16 Wild Mushroom Risotto: rice(23), mushroom(38), parmesan(19), butter(16), onion(32)
(16, 23, 150.00),
(16, 38, 100.00),
(16, 19, 30.00),
(16, 16, 20.00),
(16, 32, 20.00),

-- 17 Margherita Pizza: flour(26), tomato(31), mozzarella(18), basil(58), olive oil(51)
(17, 26, 200.00),
(17, 31, 100.00),
(17, 18, 120.00),
(17, 58, 5.00),
(17, 51, 10.00),

-- 18 Bacon and Cheese Pizza: flour(26), tomato(31), mozzarella(18), bacon(6)
(18, 26, 200.00),
(18, 31, 90.00),
(18, 18, 100.00),
(18, 6, 80.00),

-- 19 Seared Tuna Steak: tuna(10), lemon(44), olive oil(51)
(19, 10, 220.00),
(19, 44, 15.00),
(19, 51, 10.00),

-- 20 Garlic Butter Mussels: mussels(13), garlic(33), butter(16), lemon(44)
(20, 13, 250.00),
(20, 33, 10.00),
(20, 16, 20.00),
(20, 44, 15.00),

-- 21 Grilled Beef Sirloin Steak: beef sirloin(3), black pepper(57), olive oil(51)
(21, 3, 280.00),
(21, 57, 3.00),
(21, 51, 10.00),

-- 22 Duck Breast with Orange Glaze: duck breast(7), orange(45), honey(55)
(22, 7, 240.00),
(22, 45, 30.00),
(22, 55, 20.00),

-- 23 Creamy Mashed Potato: potato(27), butter(16), milk(15)
(23, 27, 200.00),
(23, 16, 20.00),
(23, 15, 50.00),

-- 24 Grilled Seasonal Vegetables: zucchini(40), bell pepper(35), broccoli(36), olive oil(51)
(24, 40, 80.00),
(24, 35, 70.00),
(24, 36, 60.00),
(24, 51, 10.00),

-- 25 Classic Apple Pie: flour(26), apple(42), sugar(53), butter(16)
(25, 26, 60.00),
(25, 42, 100.00),
(25, 53, 20.00),
(25, 16, 30.00),

-- 26 Greek Yogurt with Honey and Walnuts: greek yogurt(22), honey(55), walnuts(50), strawberry(46)
(26, 22, 150.00),
(26, 55, 20.00),
(26, 50, 20.00),
(26, 46, 30.00),

-- 27 Fresh Baked Bread: flour(26), salt(54), sugar(53)
(27, 26, 300.00),
(27, 54, 5.00),
(27, 53, 10.00),

-- 28 Almond Croissant: flour(26), butter(16), almonds(49), sugar(53)
(28, 26, 60.00),
(28, 16, 30.00),
(28, 49, 20.00),
(28, 53, 10.00),

-- 29 Crispy Chicken Nuggets: chicken breast(1), egg(14), flour(26)
(29, 1, 120.00),
(29, 14, 30.00),
(29, 26, 30.00),

-- 30 Chickpea and Vegetable Salad: chickpeas(47), tomato(31), cucumber(39), olive oil(51), lemon(44)
(30, 47, 150.00),
(30, 31, 50.00),
(30, 39, 50.00),
(30, 51, 10.00),
(30, 44, 10.00),

-- 31 Lentil Soup: lentils(48), carrot(34), onion(32), garlic(33)
(31, 48, 150.00),
(31, 34, 50.00),
(31, 32, 40.00),
(31, 33, 5.00),

-- 32 Fresh Orange Juice: orange(45)
(32, 45, 300.00),

-- 33 Homemade Lemonade: lemon(44), sugar(53), honey(55)
(33, 44, 50.00),
(33, 53, 20.00),
(33, 55, 10.00),

-- 34 Cappuccino: milk(15)
(34, 15, 100.00),

-- 35 Virgin Lemon Fizz: lemon(44), sugar(53)
(35, 44, 40.00),
(35, 53, 15.00),

-- 38 Chef's Salmon Special: salmon(9), avocado(41), lemon(44), olive oil(51)
(38, 9, 220.00),
(38, 41, 60.00),
(38, 44, 10.00),
(38, 51, 10.00);

-- Note: items 36 (House Red Wine Glass) and 37 (Chardonnay) intentionally
-- have no rows in menu_item_ingredients, since wine/alcohol is not modeled
-- as a menu.ingredients entry in the reference schema.
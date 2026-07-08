INSERT INTO menu.ingredients (id, name, calories_per_100g)
VALUES
-- Meat & Poultry
(1, 'Chicken Breast', 165.00),
(2, 'Chicken Thigh', 209.00),
(3, 'Beef Sirloin', 271.00),
(4, 'Ground Beef', 250.00),
(5, 'Pork Loin', 242.00),
(6, 'Bacon', 541.00),
(7, 'Duck Breast', 337.00),
(8, 'Lamb Chop', 294.00),

-- Fish & Seafood
(9, 'Salmon Fillet', 208.00),
(10, 'Tuna', 132.00),
(11, 'Shrimp', 99.00),
(12, 'Cod', 82.00),
(13, 'Mussels', 172.00),

-- Dairy & Eggs
(14, 'Chicken Egg', 155.00),
(15, 'Whole Milk', 61.00),
(16, 'Butter', 717.00),
(17, 'Cheddar Cheese', 402.00),
(18, 'Mozzarella Cheese', 280.00),
(19, 'Parmesan Cheese', 431.00),
(20, 'Cream Cheese', 342.00),
(21, 'Heavy Cream', 340.00),
(22, 'Greek Yogurt', 59.00),

-- Grains & Starches
(23, 'White Rice', 130.00),
(24, 'Brown Rice', 111.00),
(25, 'Pasta', 158.00),
(26, 'Wheat Flour', 364.00),
(27, 'Potato', 77.00),
(28, 'Sweet Potato', 86.00),
(29, 'Oats', 389.00),
(30, 'Bread', 265.00),

-- Vegetables
(31, 'Tomato', 18.00),
(32, 'Onion', 40.00),
(33, 'Garlic', 149.00),
(34, 'Carrot', 41.00),
(35, 'Bell Pepper', 31.00),
(36, 'Broccoli', 34.00),
(37, 'Spinach', 23.00),
(38, 'Mushroom', 22.00),
(39, 'Cucumber', 15.00),
(40, 'Zucchini', 17.00),
(41, 'Avocado', 160.00),

-- Fruits
(42, 'Apple', 52.00),
(43, 'Banana', 89.00),
(44, 'Lemon', 29.00),
(45, 'Orange', 47.00),
(46, 'Strawberry', 32.00),

-- Legumes & Nuts
(47, 'Chickpeas', 164.00),
(48, 'Lentils', 116.00),
(49, 'Almonds', 579.00),
(50, 'Walnuts', 654.00),

-- Fats & Oils
(51, 'Olive Oil', 884.00),
(52, 'Sunflower Oil', 884.00),

-- Seasonings & Others
(53, 'Sugar', 387.00),
(54, 'Salt', 0.00),
(55, 'Honey', 304.00),
(56, 'Soy Sauce', 53.00),
(57, 'Black Pepper', 251.00),
(58, 'Basil', 22.00);

SELECT setval('menu.ingredients_id_seq', (SELECT MAX(id) FROM menu.ingredients));
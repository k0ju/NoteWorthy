INSERT INTO
	icon("id", "name")
VALUES
	(1, 'mdi-pot-steam'),
	(2, 'mdi-checkbox-marked'),
	(3, 'mdi-basket-check'),
	(4, 'mdi-calendar-today'),
	(5, 'mdi-lightbulb'),
	(6, 'mdi-account-box'),
	(7, 'mdi-forest'),
	(8, 'mdi-paw'),
	(9, 'mdi-music-box'),
	(10, 'mdi-map-marker'),
	(11, 'mdi-heart')
ON CONFLICT ("id") DO NOTHING;
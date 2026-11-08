-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 11-05-2026 a las 20:51:12
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `videoclub`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alquiler`
--

CREATE TABLE `alquiler` (
  `id` bigint(20) NOT NULL,
  `fechaAlquiler` date DEFAULT NULL,
  `fechaDevolucion` date DEFAULT NULL,
  `id_pelicula` bigint(20) DEFAULT NULL,
  `id_socio` bigint(20) DEFAULT NULL,
  `id_videojuego` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alquiler`
--

INSERT INTO `alquiler` (`id`, `fechaAlquiler`, `fechaDevolucion`, `id_pelicula`, `id_socio`, `id_videojuego`) VALUES
(1, '2026-05-07', '2026-05-10', 1, 2, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pelicula`
--

CREATE TABLE `pelicula` (
  `id` bigint(20) NOT NULL,
  `anio` int(11) NOT NULL,
  `director` varchar(255) NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `valoracion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pelicula`
--

INSERT INTO `pelicula` (`id`, `anio`, `director`, `titulo`, `valoracion`) VALUES
(1, 2010, 'Christopher Nolan', 'Inception', 9),
(2, 1999, 'Lana Wachowski', 'The Matrix', 10),
(3, 1994, 'Quentin Tarantino', 'Pulp Fiction', 9),
(4, 2014, 'Christopher Nolan', 'Interstellar', 9),
(5, 1972, 'Francis Ford Coppola', 'El Padrino', 10),
(6, 1995, 'David Fincher', 'Seven', 8),
(7, 2000, 'Ridley Scott', 'Gladiator', 9),
(8, 2017, 'Denis Villeneuve', 'Blade Runner 2049', 8),
(9, 1979, 'Ridley Scott', 'Alien', 9),
(10, 2008, 'Christopher Nolan', 'The Dark Knight', 10),
(11, 2001, 'Peter Jackson', 'El señor de los anillos', 10),
(15, 2019, 'Hermanos Russo', 'Avengers: Endgame', 9),
(16, 2019, 'Bong Joon-ho', 'Parasite', 10),
(17, 2022, 'Darren Aronofsky', 'The Whale', 8),
(18, 2021, 'Jon Watts', 'Spiderman: No Way Home', 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `socio`
--

CREATE TABLE `socio` (
  `id` bigint(20) NOT NULL,
  `dni` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `socio`
--

INSERT INTO `socio` (`id`, `dni`, `nombre`) VALUES
(1, '12345678Z', 'Adrián Martínez (Socio Admin)'),
(2, '00000000E', 'Pepe Pérez');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `videojuego`
--

CREATE TABLE `videojuego` (
  `id` bigint(20) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `plataforma` varchar(50) NOT NULL,
  `anio` int(11) NOT NULL,
  `valoracion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `videojuego`
--

INSERT INTO `videojuego` (`id`, `titulo`, `plataforma`, `anio`, `valoracion`) VALUES
(1, 'The Legend of Zelda: TotK', 'Nintendo Switch', 2023, 10),
(2, 'Elden Ring', 'PS5/PC', 2022, 10),
(3, 'God of War Ragnarok', 'PS5', 2022, 9),
(4, 'Red Dead Redemption 2', 'PC/Xbox/PS4', 2018, 10);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alquiler`
--
ALTER TABLE `alquiler`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKfhutsj0rdfvvu33jt1cdj99wy` (`id_pelicula`),
  ADD KEY `FKqunshkoym6n4oasp5crfqcutb` (`id_socio`),
  ADD KEY `fk_alquiler_videojuego` (`id_videojuego`);

--
-- Indices de la tabla `pelicula`
--
ALTER TABLE `pelicula`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `socio`
--
ALTER TABLE `socio`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_iyfehmpa2qw82c2h79wve9aab` (`dni`);

--
-- Indices de la tabla `videojuego`
--
ALTER TABLE `videojuego`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alquiler`
--
ALTER TABLE `alquiler`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `pelicula`
--
ALTER TABLE `pelicula`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT de la tabla `socio`
--
ALTER TABLE `socio`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `videojuego`
--
ALTER TABLE `videojuego`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alquiler`
--
ALTER TABLE `alquiler`
  ADD CONSTRAINT `FKfhutsj0rdfvvu33jt1cdj99wy` FOREIGN KEY (`id_pelicula`) REFERENCES `pelicula` (`id`),
  ADD CONSTRAINT `FKqunshkoym6n4oasp5crfqcutb` FOREIGN KEY (`id_socio`) REFERENCES `socio` (`id`),
  ADD CONSTRAINT `fk_alquiler_videojuego` FOREIGN KEY (`id_videojuego`) REFERENCES `videojuego` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

package app;

import dao.PeliculaDAO;
import model.Alquiler;
import model.Pelicula;
import model.Socio;
import model.Videojuego;

import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de usuario final para la gestión del Videoclub.
 * Cubre Películas, Videojuegos, Socios y Alquileres (UP5, UP12).
 *
 * @author Adrián Martínez
 * @version 1.4
 */
public class Main {
    private static PeliculaDAO dao = new PeliculaDAO();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarDatos();

        int opcion = 0;
        do {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1 -> agregarPelicula();
                    case 2 -> listarPeliculas();
                    case 3 -> agregarVideojuego();
                    case 4 -> listarVideojuegos();
                    case 5 -> registrarSocio();
                    case 6 -> listarSocios();
                    case 7 -> realizarAlquilerPelicula();
                    case 8 -> realizarAlquilerVideojuego();
                    case 9 -> reporteAlquileresPeliculas();
                    case 10 -> reporteAlquileresVideojuegos();
                    case 11 -> eliminarPelicula();
                    case 12 -> eliminarVideojuego();
                    case 13 -> buscarPeliculasPorAnio();
                    case 14 -> buscarPeliculasPorValoracion();
                    case 15 -> buscarVideojuegosPorAnio();
                    case 16 -> buscarVideojuegosPorValoracion();
                    case 17 -> modificarPelicula();
                    case 18 -> modificarVideojuego();
                    case 19 -> devolverAlquiler();
                    case 20 -> System.out.println("Cerrando el sistema profesional...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Formato incorrecto.");
            }
        } while (opcion != 20);
    }

    private static void mostrarMenu() {
        System.out.println("\n=======================================================");
        System.out.println("          SISTEMA DE GESTIÓN RETROVIDEO v1.5           ");
        System.out.println("=======================================================");
        System.out.println("1. Añadir Película         2. Listar Películas");
        System.out.println("3. Añadir Videojuego       4. Listar Videojuegos");
        System.out.println("5. Registrar Socio         6. Listar Socios");
        System.out.println("-------------------------------------------------------");
        System.out.println("7. ALQUILAR PELÍCULAS      8. ALQUILAR VIDEOJUEGOS");
        System.out.println("-------------------------------------------------------");
        System.out.println("9.  Películas Alquiladas   10. Videojuegos Alquilados ");
        System.out.println("11. Eliminar Película      12. Eliminar Videojuego");
        System.out.println("13. Buscar Peli por Año    14. Buscar Peli por Val.");
        System.out.println("15. Buscar Juego por Año   16. Buscar Juego por Val.");
        System.out.println("17. Modificar Película     18. Modificar Videojuego");
        System.out.println("19. Devolver Alquiler      20. Salir");
        System.out.print("Selecciona una opción: ");
    }

    private static void inicializarDatos() {
        if (dao.listarTodas().isEmpty()) {
            dao.guardar(new Pelicula("Interstellar", "Christopher Nolan", 2014, 9));
            dao.guardar(new Pelicula("The Whale", "Darren Aronofsky", 2022, 8));
            dao.guardar(new Pelicula("Parasite", "Bong Joon-ho", 2019, 10));
        }
        if (dao.listarVideojuegos().isEmpty()) {
            dao.guardarVideojuego(new Videojuego("Zelda: TotK", "Switch", 2023, 10));
            dao.guardarVideojuego(new Videojuego("Elden Ring", "PS5/PC", 2022, 10));
        }
        if (dao.listarSocios().isEmpty()) {
            dao.guardarSocio(new Socio("12345678Z", "Adrián Martínez"));
        }
    }

    // --- GESTIÓN DE PELÍCULAS ---
    private static void agregarPelicula() {
        System.out.print("Título: "); String t = sc.nextLine();
        System.out.print("Director: "); String d = sc.nextLine();
        int a = leerEntero("Año: ", 1895, 2026);
        int v = leerEntero("Valoración (1-10): ", 1, 10);
        dao.guardar(new Pelicula(t, d, a, v));
        System.out.println("✅ Película añadida.");
    }

    private static void listarPeliculas() {
        System.out.println("\n--- CATÁLOGO DE PELÍCULAS ---");
        System.out.printf("%-5s | %-25s | %-20s | %-5s | %-5s%n", "ID", "TÍTULO", "DIRECTOR", "AÑO", "NOTA");
        for (Pelicula p : dao.listarTodas()) {
            System.out.printf("%-5d | %-25s | %-20s | %-5d | %-5d%n", p.getId(), p.getTitulo(), p.getDirector(), p.getAnio(), p.getValoracion());
        }
    }

    private static void buscarPeliculasPorAnio() {
        int anio = leerEntero("Introduce el año a buscar: ", 1895, 2026);
        List<Pelicula> resultados = dao.listarPorAnio(anio);
        System.out.println("\n--- RESULTADOS DEL AÑO " + anio + " ---");
        mostrarResultadosPeliculas(resultados);
    }

    private static void buscarPeliculasPorValoracion() {
        int valMinima = leerEntero("Introduce la valoración mínima a buscar (1-10): ", 1, 10);
        List<Pelicula> resultados = dao.consultaPersonalizada(valMinima);
        System.out.println("\n--- PELÍCULAS CON VALORACIÓN MAYOR A " + valMinima + " ---");
        mostrarResultadosPeliculas(resultados);
    }

    private static void mostrarResultadosPeliculas(List<Pelicula> resultados) {
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron resultados.");
        } else {
            System.out.printf("%-5s | %-25s | %-20s | %-5s | %-5s%n", "ID", "TÍTULO", "DIRECTOR", "AÑO", "NOTA");
            for (Pelicula p : resultados) {
                System.out.printf("%-5d | %-25s | %-20s | %-5d | %-5d%n", p.getId(), p.getTitulo(), p.getDirector(), p.getAnio(), p.getValoracion());
            }
        }
    }

    private static void modificarPelicula() {
        listarPeliculas();
        System.out.print("Introduce el ID de la película a modificar: ");
        Long id = Long.parseLong(sc.nextLine());
        Pelicula p = dao.buscarPorId(id);

        if (p != null) {
            System.out.println("Editando: " + p.getTitulo());
            System.out.print("Nuevo Título: "); p.setTitulo(sc.nextLine());
            System.out.print("Nuevo Director: "); p.setDirector(sc.nextLine());
            p.setAnio(leerEntero("Nuevo Año: ", 1895, 2026));
            p.setValoracion(leerEntero("Nueva Valoración (1-10): ", 1, 10));
            dao.modificar(p);
            System.out.println("✅ Película modificada correctamente.");
        } else {
            System.out.println("❌ Error: ID no encontrado.");
        }
    }

    private static void eliminarPelicula() {
        listarPeliculas();
        System.out.print("Introduce el ID de la película a eliminar: ");
        Long id = Long.parseLong(sc.nextLine());
        dao.eliminarPelicula(id);
        System.out.println("✅ Película eliminada correctamente del sistema.");
    }

    // --- GESTIÓN DE VIDEOJUEGOS ---
    private static void agregarVideojuego() {
        System.out.print("Título: "); String t = sc.nextLine();
        System.out.print("Plataforma: "); String p = sc.nextLine();
        int a = leerEntero("Año: ", 1950, 2026);
        int v = leerEntero("Valoración (1-10): ", 1, 10);
        dao.guardarVideojuego(new Videojuego(t, p, a, v));
        System.out.println("✅ Videojuego añadido.");
    }

    private static void listarVideojuegos() {
        System.out.println("\n--- CATÁLOGO DE VIDEOJUEGOS ---");
        System.out.printf("%-5s | %-25s | %-15s | %-5s | %-5s%n", "ID", "TÍTULO", "PLATAFORMA", "AÑO", "NOTA");
        for (Videojuego v : dao.listarVideojuegos()) {
            System.out.printf("%-5d | %-25s | %-15s | %-5d | %-5d%n", v.getId(), v.getTitulo(), v.getPlataforma(), v.getAnio(), v.getValoracion());
        }
    }

    private static void buscarVideojuegosPorAnio() {
        int anio = leerEntero("Introduce el año a buscar: ", 1950, 2026);
        List<Videojuego> resultados = dao.listarVideojuegosPorAnio(anio);
        System.out.println("\n--- RESULTADOS DEL AÑO " + anio + " ---");
        mostrarResultadosVideojuegos(resultados);
    }

    private static void buscarVideojuegosPorValoracion() {
        int valMinima = leerEntero("Introduce la valoración mínima a buscar (1-10): ", 1, 10);
        List<Videojuego> resultados = dao.consultaPersonalizadaVideojuego(valMinima);
        System.out.println("\n--- VIDEOJUEGOS CON VALORACIÓN MAYOR A " + valMinima + " ---");
        mostrarResultadosVideojuegos(resultados);
    }

    private static void mostrarResultadosVideojuegos(List<Videojuego> resultados) {
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron resultados.");
        } else {
            System.out.printf("%-5s | %-25s | %-15s | %-5s | %-5s%n", "ID", "TÍTULO", "PLATAFORMA", "AÑO", "NOTA");
            for (Videojuego v : resultados) {
                System.out.printf("%-5d | %-25s | %-15s | %-5d | %-5d%n", v.getId(), v.getTitulo(), v.getPlataforma(), v.getAnio(), v.getValoracion());
            }
        }
    }

    private static void modificarVideojuego() {
        listarVideojuegos();
        System.out.print("Introduce el ID del videojuego a modificar: ");
        Long id = Long.parseLong(sc.nextLine());
        Videojuego v = dao.buscarVideojuegoPorId(id);

        if (v != null) {
            System.out.println("Editando: " + v.getTitulo());
            System.out.print("Nuevo Título: "); v.setTitulo(sc.nextLine());
            System.out.print("Nueva Plataforma: "); v.setPlataforma(sc.nextLine());
            v.setAnio(leerEntero("Nuevo Año: ", 1950, 2026));
            v.setValoracion(leerEntero("Nueva Valoración (1-10): ", 1, 10));
            dao.modificarVideojuego(v);
            System.out.println("✅ Videojuego modificado correctamente.");
        } else {
            System.out.println("❌ Error: ID no encontrado.");
        }
    }

    private static void eliminarVideojuego() {
        listarVideojuegos();
        System.out.print("Introduce el ID del videojuego a eliminar: ");
        Long id = Long.parseLong(sc.nextLine());
        dao.eliminarVideojuego(id);
        System.out.println("✅ Videojuego eliminado correctamente del sistema.");
    }

    // --- GESTIÓN DE SOCIOS ---
    private static void registrarSocio() {
        System.out.print("DNI (ej. 12345678Z): ");
        String dni = sc.nextLine().trim().toUpperCase();
        if (!dni.matches("^[0-9]{8}[A-Z]$")) {
            System.out.println("❌ Error: El DNI debe tener 8 números seguidos de una letra.");
            return;
        }
        if (dao.buscarSocioPorDni(dni) != null) {
            System.out.println("❌ Error: Ya existe un socio registrado con el DNI " + dni + ".");
            return;
        }
        System.out.print("Nombre completo: ");
        String n = sc.nextLine().trim();
        dao.guardarSocio(new Socio(dni, n));
        System.out.println("✅ Socio registrado correctamente.");
    }

    private static void listarSocios() {
        System.out.println("\n--- LISTADO DE SOCIOS REGISTRADOS ---");
        System.out.printf("%-5s | %-12s | %-30s%n", "ID", "DNI", "NOMBRE");
        for (Socio s : dao.listarSocios()) {
            System.out.printf("%-5d | %-12s | %-30s%n", s.getId(), s.getDni(), s.getNombre());
        }
    }

    // --- PROCESOS DE ALQUILER ---
    private static void realizarAlquilerPelicula() {
        listarPeliculas();
        System.out.print("ID Película: "); Long idP = Long.parseLong(sc.nextLine());
        System.out.print("DNI Socio: "); String dni = sc.nextLine();

        Pelicula p = dao.buscarPorId(idP);
        Socio s = dao.buscarSocioPorDni(dni);

        if (p != null && s != null) {
            dao.registrarAlquiler(new Alquiler(p, s));
            System.out.println("✅ Alquiler de película registrado.");
        } else System.out.println("❌ Error: Datos no encontrados.");
    }

    private static void realizarAlquilerVideojuego() {
        listarVideojuegos();
        System.out.print("ID Videojuego: "); Long idV = Long.parseLong(sc.nextLine());
        System.out.print("DNI Socio: "); String dni = sc.nextLine();

        Videojuego v = dao.buscarVideojuegoPorId(idV);
        Socio s = dao.buscarSocioPorDni(dni);

        if (v != null && s != null) {
            dao.registrarAlquiler(new Alquiler(v, s));
            System.out.println("✅ Alquiler de videojuego registrado.");
        } else System.out.println("❌ Error: Datos no encontrados.");
    }

    // --- INFORMES DETALLADOS ---
    private static void reporteAlquileresPeliculas() {
        System.out.println("\n--- INFORME: PELÍCULAS ALQUILADAS ---");
        System.out.printf("%-25s | %-12s | %-15s%n", "PELÍCULA", "DNI SOCIO", "FECHA DEV.");
        for (Alquiler a : dao.listarAlquileresPeliculas()) {
            System.out.printf("%-25s | %-12s | %-15s%n",
                    a.getPelicula().getTitulo(), a.getSocio().getDni(), a.getFechaDevolucion());
        }
    }

    private static void reporteAlquileresVideojuegos() {
        System.out.println("\n--- INFORME: VIDEOJUEGOS ALQUILADOS ---");
        System.out.printf("%-5s | %-25s | %-12s | %-15s%n", "ID", "JUEGO", "DNI SOCIO", "FECHA DEV.");
        for (Alquiler a : dao.listarAlquileresVideojuegos()) {
            System.out.printf("%-5d | %-25s | %-12s | %-15s%n",
                    a.getId(), a.getVideojuego().getTitulo(), a.getSocio().getDni(), a.getFechaDevolucion());
        }
    }

    private static void devolverAlquiler() {
        List<Alquiler> activos = dao.listarTodosAlquileres();
        if (activos.isEmpty()) {
            System.out.println("No hay alquileres activos pendientes de devolución.");
            return;
        }
        System.out.println("\n--- ALQUILERES ACTIVOS ---");
        for (Alquiler a : activos) {
            System.out.println(a);
        }
        System.out.print("Introduce el ID del alquiler a devolver: ");
        Long idAlquiler = Long.parseLong(sc.nextLine());
        if (dao.devolverAlquiler(idAlquiler)) {
            System.out.println("✅ Devolución procesada correctamente. El artículo vuelve a estar disponible.");
        } else {
            System.out.println("❌ Error: No se encontró ningún alquiler con ID " + idAlquiler + ".");
        }
    }

    private static int leerEntero(String m, int min, int max) {
        while (true) {
            try {
                System.out.print(m);
                int n = Integer.parseInt(sc.nextLine());
                if (n >= min && n <= max) return n;
                System.out.println("Rango inválido.");
            } catch (Exception e) { System.out.println("Introduce un número."); }
        }
    }
}
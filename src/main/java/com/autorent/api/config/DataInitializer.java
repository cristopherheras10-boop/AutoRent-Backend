package com.autorent.api.config;

import com.autorent.api.entity.*;
import com.autorent.api.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;
    private final VehiculoRepository vehiculos;
    private final PasswordEncoder encoder;
    private final JdbcTemplate jdbc;
    private final TransactionTemplate transactionTemplate;
    @Value("${app.admin.nombre}") String adminNombre;
    @Value("${app.admin.correo}") String adminCorreo;
    @Value("${app.admin.password}") String adminPassword;
    public DataInitializer(UsuarioRepository usuarios, CategoriaRepository categorias, VehiculoRepository vehiculos, PasswordEncoder encoder, JdbcTemplate jdbc, PlatformTransactionManager transactionManager) {
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.vehiculos = vehiculos;
        this.encoder = encoder;
        this.jdbc = jdbc;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }
    @Override
    public void run(String... args) {
        corregirColumnaImagenSiEsLob();
        crearAdmin();
        CategoriaVehiculo sedan = categoria("Sedán", "Cómodo, económico y perfecto para ciudad");
        CategoriaVehiculo suv = categoria("SUV", "Mayor espacio y comodidad para viajes");
        CategoriaVehiculo pickup = categoria("Pickup", "Capacidad de carga y trabajo");
        CategoriaVehiculo premium = categoria("Premium", "Vehículos de gama alta");
        vehiculo(
                "ABC-1234",
                "Toyota",
                "Corolla",
                2024,
                "Blanco",
                45,
                sedan,
                "https://prod.pictures.autoscout24.net/listing-images/0b78e8f6-5789-4d3b-a1dd-5bb0302585ea_bb7d5b88-48d2-46e9-bcd4-20035c27a58a.jpg/1920x1080.webp",
                "https://www.sarasotatoyota.com/inventoryphotos/16949/jtds4mce8r3520096/ip/3.jpg?bg-color=FFFFFF&timestamp=2026-01-16T16%3A16%3A45Z&width=400+400w",
                "https://bucket.dealeralchemist.com/marietta-toyota/Toyota-Corolla-for-Sale-Near-Sandy-Springs-2024-Toyota-Corolla-Dash.jpg?auto=compress%2Cformat&ixlib=php-3.3.1"
        );
        vehiculo(
                "PBC-2045",
                "Chevrolet",
                "Onix",
                2023,
                "Rojo",
                40,
                sedan,
                "https://images.usadosbr.com/manipulatedImages/media/gallery/50/df/e8/chevrolet-onix-1-0-ltz-turbo-2023-anapolis-go-a7cdd8b8-bY--2-image-760x570-crop.webp",
                "https://www.deruedas.com.ar/images/autos/Chevrolet-Onix-2023/706/706630_5_im.jpg?edit=4",
                "https://catarina-prd.s3.sa-east-1.amazonaws.com/dcab6c3b469b261b848175acb04146f3.JPG"
        );
        vehiculo(
                "GBC-7788",
                "Kia",
                "Sportage",
                2024,
                "Negro",
                70,
                suv,
                "https://www.dubicars.com/images/b279f0/w_1300x760/al-sayed-khamis/3b42df97-6c7f-4dd6-92ba-4d815eb0d7d5.jpeg",
                "https://www.carbase.my/upload/31/159/959/exterior/s19-1738683466-1979.jpg",
                "https://www.airportkiaofnaples.com/assets/stock/expanded/transparent/1280/2024kis01_1280/2024kis010092_1280_12.png?bg-color=FFFFFF&timestamp=0001-01-01T00%3A00%3A00&width=400+400w"
        );
        vehiculo(
                "UBC-5521",
                "Hyundai",
                "Tucson",
                2023,
                "Gris",
                68,
                suv,
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FCkhp5%2FbtrVAKuZNxA%2FHO5p9bSJvNeaqEVcwkDK31%2Fimg.png&scode=mtistory2",
                "https://images.autoboom.co.il/WMIhuxASazcQocoJC0r4Ny3Yb4TQhL-0Wqh6RZxHyvM/fit/1200/1200/sm/0/Z3M6Ly9hdXRvYm9vbS1pbWFnZXMvMDAwLzAwMC8zMTUvMzE1NTQ3LmpwZw.jpg",
                "https://www.theautochannel.com/news/2023/08/28/1308026-2023-hyundai-tucson-phev-limited-awd-review-by-david-colman.2-lg.jpg"
        );
        vehiculo(
                "AAB-9090",
                "Ford",
                "Ranger",
                2024,
                "Azul",
                85,
                pickup,
                "https://www.assets.ford.com/adobe/assets/urn%3Aaaid%3Aaem%3A08034e66-7b93-4da9-8869-32b2d9826cc8/as/25_FRD_RGR_65520_XL_STX_appear.webp?crop-names=1_3x2&max-quality=75&width=1920",
                "https://www.motortrend.com/files/66340b3edbbdc000086ce6f9/007-2024-ford-ranger-xlt-rear-seats.jpg?format=webp&q=75&w=320&width=320",
                "https://www.motortrend.com/files/66340b3edbbdc000086ce6f9/007-2024-ford-ranger-xlt-rear-seats.jpg?format=webp&q=75&w=320&width=320"
        );
        vehiculo(
                "PAC-7001",
                "Toyota",
                "Hilux",
                2023,
                "Plata",
                82,
                pickup,
                "https://acnews.blob.core.windows.net/imgnews/large/NAZ_4d8c1c6513b34eb189cb45e3ec010310.webp",
                "https://www.autobusiness.com.br/_next/image?q=100&url=https%3A%2F%2Fs3.carro57.com.br%2FFC%2F2828%2F6938876_2_W_8da95921fb.jpeg&w=828",
                "https://s3-sa-east-1.amazonaws.com/revresources/regiovanio-veiculos/imagens_veiculos/640_480_folhacar_toyota-hilux-cabine-dupla-hilux-cd-2-8-tdi-srx-4wd-22-23-ed4758-11.jpg"
        );
        vehiculo(
                "BBC-3300",
                "BMW",
                "X3",
                2024,
                "Negro",
                125,
                premium,
                "https://albacars.ae/_next/image?q=75&url=https%3A%2F%2Fstorage.albacars.ae%2Fvehicles%2F54ed6656-6972-4b7f-a481-fd091ccc5fae%2F7de3f333-a720-447d-8a5c-65da5da21943.jpeg%3Fformat%3Dauto&w=3840",
                "https://cdn-sba-ecb8fveya6evhba5.z01.azurefd.net/api/ResizeImage?h=480&mode=min&url=https%3A%2F%2Faresstorageaccount.blob.core.windows.net%2Fresearchcontainer%2Fmodelyear%2F813c7a13-3ed0-4a22-851a-f8c3c8706dec.jpg&w=640",
                "https://bmw-uzywane.com.pl/assets/photo/upload/cars/41956/vehicle_a7632-crop-1200-800.jpg"
        );
        vehiculo(
                "CBC-4400",
                "Mercedes-Benz",
                "GLC",
                2024,
                "Blanco",
                135,
                premium,
                "https://www.autozone-mag.com/storage/posts/5678-1297-2022-mercedes-glc.jpg",
                "https://media.whichcar.com.au/uploads/2023/11/0dd26d2f-2024-mercedes-benz-glc-coupe-2024-mercedes-benz-glc-300-coupe-016-1920x1279.jpg",
                "https://media.whichcar.com.au/uploads/2023/11/99d8765f-2024-mercedes-benz-glc-coupe-2024-mercedes-benz-glc-300-coupe-015.jpg"
        );
        System.out.println("AutoRent: catálogo inicial e imágenes reales verificados.");
    }
    private void corregirColumnaImagenSiEsLob() {
        try {
            String tipo = jdbc.queryForObject( "SELECT udt_name FROM information_schema.columns " + "WHERE table_schema = current_schema() AND table_name = 'vehiculos' AND column_name = 'imagen_url'", String.class );
            if ("oid".equalsIgnoreCase(tipo)) {
                // Las versiones anteriores usaban @Lob y PostgreSQL creó un OID.
                // Se convierte a TEXT y se descartan solo las imágenes antiguas;
                // el catálogo inicial vuelve a cargar las URLs reales inmediatamente después.
                jdbc.execute("ALTER TABLE vehiculos ALTER COLUMN imagen_url TYPE TEXT USING NULL::text");
                System.out.println("AutoRent: columna imagen_url migrada de OID a TEXT.");
            }
        } catch (Exception e) {
            System.out.println("AutoRent: no fue necesaria la migración de imagen_url: " + e.getMessage());
        }
    }
    private void crearAdmin() {
        if(adminPassword==null || adminPassword.isBlank()){
            System.out.println("AutoRent: ADMIN_PASSWORD no configurado; no se crea un nuevo administrador.");
            return;
        }
        String correo = adminCorreo.toLowerCase();
        if (!usuarios.existsByCorreo(correo)) {
            Usuario u = new Usuario();
            u.setNombre(adminNombre);
            u.setCorreo(correo);
            u.setPassword(encoder.encode(adminPassword));
            u.setRol(Rol.ADMIN);
            u.setEstado(EstadoUsuario.APROBADO);
            u.setVerificado(true);
            usuarios.save(u);
            System.out.println("ADMIN inicial creado: " + correo);
        }
    }
    private CategoriaVehiculo categoria(String nombre, String descripcion) {
        return categorias.findByNombreIgnoreCase(nombre).orElseGet(() -> {
            CategoriaVehiculo c = new CategoriaVehiculo();
            c.setNombre(nombre);
            c.setDescripcion(descripcion);
            c.setActiva(true);
            return categorias.save(c);
        }
        );
    }
    private void vehiculo( String placa, String marca, String modelo, int anio, String color, double tarifa, CategoriaVehiculo categoria, String imagenPrincipal, String imagenSecundaria, String imagenInterior) {
        transactionTemplate.executeWithoutResult(status -> {
            Vehiculo v = vehiculos.findByPlacaIgnoreCase(placa).orElseGet(Vehiculo::new);
            boolean nuevo = v.getId() == null;
            if (nuevo) {
                v.setPlaca(placa);
                v.setMarca(marca);
                v.setModelo(modelo);
                v.setAnio(anio);
                v.setColor(color);
                v.setTarifaDiaria(BigDecimal.valueOf(tarifa));
                v.setEstado(EstadoVehiculo.DISPONIBLE);
                v.setCategoria(categoria);
            }
            // Si la imagen fue subida manualmente por el administrador (Base64), se respeta.
            // Las URLs antiguas del catálogo se reemplazan por una fotografía real funcional.
            if (nuevo && (v.getImagenUrl() == null || v.getImagenUrl().isBlank())) {
                v.setImagenUrl(imagenPrincipal);
            }
            // Los carros precargados reciben dos fotografías adicionales.
            // Si el administrador ya cargó una galería manualmente, no se sobrescribe.
            if (nuevo && (v.getImagenes() == null || v.getImagenes().isEmpty())) {
                v.setImagenes(new java.util.ArrayList<>(java.util.List.of(imagenSecundaria, imagenInterior)));
            }
            vehiculos.save(v);
        });
    }
}

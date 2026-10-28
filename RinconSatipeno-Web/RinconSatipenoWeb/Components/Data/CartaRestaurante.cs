using System.Collections.Generic;
using System.Linq;

namespace RinconSatipenoWeb.Components.Data;

public sealed record PlatoCarta(int Id, string Nombre, string Categoria, decimal Precio, string Descripcion, string Imagen, string Etiqueta);

public static class CartaRestaurante
{
    public static IReadOnlyList<PlatoCarta> Platos { get; } = new List<PlatoCarta>
    {
        new(1, "Tequeños de queso (6 unidades)", "Entradas", 18m, "Seis tequeños dorados, rellenos de queso y acompañados de salsa de palta.", "/images/carta/tequenos-queso.jpg", ""),
        new(2, "Papa a la huancaína", "Entradas", 16m, "Papas tiernas cubiertas con salsa huancaína, huevo y aceituna.", "/images/carta/papa-huancaina.jpg", ""),
        new(3, "Ensalada fresca del huerto", "Entradas", 16m, "Lechuga, tomate, pepino y vegetales frescos de temporada.", "/images/carta/ensalada-huerto.jpg", "Vegetariano"),
        new(4, "Causa limeña de pollo", "Entradas", 20m, "Capas de papa amarilla, pollo deshilachado y palta.", "/images/carta/causa-pollo.jpg", ""),
        new(5, "Arroz con pollo", "Platos de fondo", 25m, "Arroz verde con culantro, presa de pollo y salsa criolla.", "/images/carta/arroz-con-pollo.jpg", ""),
        new(6, "Lomo saltado — especialidad de la casa", "Platos de fondo", 39m, "Lomo de res salteado al wok con cebolla, tomate, papas fritas y arroz.", "/images/carta/lomo-saltado.jpg", "Especialidad de la casa"),
        new(7, "Tallarines verdes con champiñones", "Platos de fondo", 28m, "Pasta en salsa de albahaca y espinaca, con champiñones salteados.", "/images/carta/tallarines-verdes.jpg", "Vegetariano"),
        new(8, "Juane de gallina", "Platos de fondo", 28m, "Arroz amazónico especiado, gallina y huevo cocidos en hoja de bijao.", "/images/carta/juane-gallina.jpg", ""),
        new(9, "Arroz chaufa de pollo", "Platos de fondo", 27m, "Arroz salteado con pollo, huevo, cebolla china y sillao.", "/images/carta/arroz-chaufa.jpg", ""),
        new(10, "Suspiro a la limeña — postre de la casa", "Postres", 14m, "Crema suave de manjar con merengue y un toque de canela.", "/images/carta/suspiro-limeno.jpg", "Postre de la casa"),
        new(11, "Mazamorra morada", "Postres", 12m, "Postre peruano de maíz morado y frutas con canela.", "/images/carta/mazamorra-morada.jpg", ""),
        new(12, "Torta de chocolate", "Postres", 15m, "Porción de bizcocho de chocolate húmedo con cobertura cremosa.", "/images/carta/torta-chocolate.jpg", ""),
        new(13, "Jugo de maracuyá", "Bebidas", 10m, "Jugo natural de maracuyá servido frío.", "/images/carta/jugo-maracuya.jpg", ""),
        new(14, "Limonada con hierbabuena", "Bebidas", 8m, "Limones frescos, hierbabuena y hielo.", "/images/carta/limonada-hierbabuena.jpg", ""),
        new(15, "Chicha morada", "Bebidas", 9m, "Bebida casera de maíz morado con frutas y especias.", "/images/carta/chicha-morada.jpg", ""),
        new(16, "Batido de lúcuma", "Bebidas", 13m, "Batido cremoso de lúcuma preparado con leche.", "/images/carta/batido-lucuma.jpg", ""),
    };

    public static PlatoCarta? Buscar(int id) => Platos.FirstOrDefault(p => p.Id == id);
}

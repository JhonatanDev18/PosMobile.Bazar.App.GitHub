Nombre del Proyecto: Bazar POS Mobile Crystal
Descripción: Pos Móvil para Bazares en Crystal realizado en Android
Autor: Ender Urdaneta
Arquitectura de presentacion: MVC

DIRECTORIOS:
./common: Clases comunes para todo el proyecto como:
    Constantes: Clase con las Contates usadas en la app.
    Logfile: Clase estatica usada para crear y almanecar informacion en un archivo txt.
    MyApp: Clase para optener la instacia de la app.
    SPM: Clase con la funciones necesarias para guarda preferencias.
    BluetoothImpresora: Clases para obtener la conexion a las impresora mediante bluetooth.
./datafono: Utilidades para usar el datafono de credibanco.
./db: Utilidades para usar la persistencia de datos room: Conexion, entity y dao.
./impresota: Utilidades para usar las impresoras en red.
./retrofit: Utilidades usadas para el consumo de servicios mediante retrofit, con los request y response.
./ui: Activitys de la app, adapter y fragments de la app.
./utilsSunmi: Utilidades para usar las impresoras de los dispositivos sumni.

VERSIONES:
"1.0.0.0":
PosMobile para el bazar, su funcionamiento es en línea, puedes configurar el dispositivo, iniciar sesión,
buscar el cliente, registrar los artículos con cantidades, realizar el pago, soportando TEF, todas las impresionas
se realizarán en los dispositivos Sunmi.

"1.0.1.0":
Se realiza la carga de medios de pagos y tarjetas bancarias al iniciar sesión, con la finalidad que en el proceso
de pago solo se consuman los servicios de creación de documento.

"1.1.0.0":
Se cambio el orden de los medios de pagos en su respectiva actividad, así como, aplicar un orden predefinido
a las tarjetas bancarias en el medio de pago tef contingencia. Ahora se soporta la impresión ESCPOS para
impresoras en red y en los dispositivos imin. Se añadió un resumen de la factura.

"1.1.0.1":
Controlando error en las cantidades del producto, las cantidades de los artículos deben ser mayor a cero.

"1.1.0.2":
Mejoras en la impresión en los equipos Sumni, controlando error en el datafono.

"2.0.0.0"
Modo autonomo, es decir realizar todo el flujo en modo offline, para despues sincronizar las ventas.
Se envian los descuentos.

"2.1.0.0"
Devoluciones con y sin informacion, con efectivo y tef contigencia, en linea y modo autonomo.
Autorizacion para las devoluciones mediante el parametro ZPOSM:BAZAR:AUTORIZADEV.
Los envio pagos tef, en caso de error no para el flujo de finaliza la compra, la informacion de los pagos tef queda en el log de las apis intermedias.

"2.2.0.0"
Control al error de finalizar documento sin enviar los pagos tef.
Suspensión de documentos online y offline.
Reimprimir  tirilla tef.
Reimprimir factura.

"2.4.0.0"
- Qr bancolombia 2.0
- Cambios visuales a la app

versión "2.4.1.3"
1.Se corrigió el problema de impresion para dispositivos Imin con Android 11.


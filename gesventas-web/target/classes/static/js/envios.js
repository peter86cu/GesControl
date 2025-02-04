$(document).ready(function() {
	// Recorrer todas las filas de la tabla y obtener los IDs de los clientes
	$("tr").each(function() {
		var idCliente = $(this).find(".idCliente").text().trim(); // Obtener el ID del cliente
		var nombreClienteCell = $(this).find(".nombreCliente"); // Celda donde se mostrará el nombre
		var datos = new FormData();

		if (idCliente) {
			datos.append("id_usuario", idCliente);

			$.ajax({
				url: globalPath + "/user-by-id-envios",
				method: "POST",
				data: datos,
				chache: false,
				contentType: false,
				processData: false,
				dataType: "json",
				success: function(respuesta) {
					var response = JSON.stringify(respuesta, null, '\t');
					var data = JSON.parse(response);
					if (data.status) {
						nombreClienteCell.text(data.user.name);


					} else {
						nombreClienteCell.text("No encontrado");

					}

				}

			})



		}
	});
});


$(document).ready(function() {
	// Recorrer todas las filas de la tabla y obtener los IDs de los clientes
	$("tr").each(function() {
		var idEstado = $(this).find(".idEstado").text().trim(); // Obtener el ID del cliente
		var estadoEnvioCell = $(this).find(".estadoEnvio"); // Celda donde se mostrará el nombre
		var datos = new FormData();

		if (idEstado) {
			datos.append("idEstado", idEstado);

			$.ajax({
				url: globalPath + "/send-status",
				method: "POST",
				data: datos,
				chache: false,
				contentType: false,
				processData: false,
				dataType: "json",
				success: function(respuesta) {
					var response = JSON.stringify(respuesta, null, '\t');
					var data = JSON.parse(response);
					if (data.status) {
						estadoEnvioCell.text(data.estadoVentas.descripcion);


					} else {
						estadoEnvioCell.text("No encontrado");

					}

				}

			})



		}
	});
});


$(document).ready(function() {
	// Recorrer todas las filas de la tabla y obtener los IDs de los clientes
	$(".verVenta").click(function(event) {
		event.preventDefault();
		let idVenta = $(this).data("idventa");
		var datos = new FormData();

		if (idVenta) {
			datos.append("idVenta", idVenta);

			$.ajax({
				url: globalPath + "/user-by-venta",
				method: "POST",
				data: datos,
				chache: false,
				contentType: false,
				processData: false,
				dataType: "json",
				success: function(respuesta) {
					var response = JSON.stringify(respuesta, null, '\t');
					var data = JSON.parse(response);
					if (data.status) {
						let contenido = `<tr><th>ID Venta:</th><td>${data.venta.id_venta}</td></tr>
						                 <tr><th>Cliente:</th><td>${data.venta.monto_total}</td></tr>
						                 <tr><th>Monto Total:</th><td>${data.venta.montoTotal}</td></tr>
						                  <tr><th>Fecha:</th><td>${data.venta.id_transaccion}</td></tr> `;
						$("#ventaDetalles").html(contenido);
						$("#modalVenta").modal("show");


					} else {
						Swal.fire({
							icon: "error",
							text: "No se encontraron datos de la venta.",

						})
					}

				}

			})



		}
	});
});


$(document).ready(function() {
	// Recorrer todas las filas de la tabla y obtener los IDs de los clientes
	$(".verDireccion").click(function(event) {
		event.preventDefault();
		let idDireccion = $(this).data("iddireccion");
		var datos = new FormData();

		if (idDireccion) {
			datos.append("idDireccion", idDireccion);

			$.ajax({
				url: globalPath + "/user-by-address",
				method: "POST",
				data: datos,
				chache: false,
				contentType: false,
				processData: false,
				dataType: "json",
				success: function(respuesta) {
					var response = JSON.stringify(respuesta, null, '\t');
					var data = JSON.parse(response);
					if (data.status) {
						let contenido = `
											<tr><th>Alias:</th><td>${data.direccion.alias}</td></tr>
											<tr><th>Ciudad:</th><td>${data.direccion.state}</td></tr>
											<tr><th>Departamento:</th><td>${data.direccion.city}</td></tr>
											<tr><th>CP:</th><td>${data.direccion.zip_code}</td></tr>
											<tr><th>CP:</th><td>${data.direccion.full_address}</td></tr>
											<tr><th>Teléfono:</th><td>${data.direccion.telefono}</td></tr>
										`;
						$("#direccionDetalles").html(contenido);
						$("#modalDireccion").modal("show");

					} else {
						Swal.fire({
							icon: "error",
							text: "No se encontró una dirección valida para esta venta.",

						})
					}

				}

			})



		}
	});
});


/**CODIGO PARA EL MODAL DE OBSERVACIONES Y GUARDAR  */

$(document).ready(function() {
	// Al hacer clic en el botón de edición
	$(".modalObservaciones").click(function() {
		let id = $(this).data("id");
		let observacion = $(this).data("observacion");

		// Guardamos el id en un campo oculto dentro del modal
		$("#envioId").val(id);
		$("#observacion").val(observacion);

		// Guardamos también el id en el botón de guardar
		$("#guardarObservacion").data("id", id);
	});

	// Al hacer clic en guardar cambios
	$("#guardarObservacion").click(function() {
		// Aquí tomamos el ID desde el campo oculto del modal
		let idEnvio = $("#envioId").val();
		let nuevaObservacion = $("#observacion").val();

		var datos = new FormData();
		datos.append("idEnvio", idEnvio);
		datos.append("observaciones", nuevaObservacion);

		$.ajax({
			url: globalPath + "/envios/actualizar-observacion",
			method: "POST",
			data: datos,
			cache: false,
			contentType: false,
			processData: false,
			dataType: "json",
			success: function(respuesta) {
				var response = JSON.stringify(respuesta, null, '\t');
				var data = JSON.parse(response);
				if (data.status) {
					location.reload();
				}
			}
		});
	});
});



/**PARA MANERAR LA ASIGNACION Y EL ESTADO DEL ENVIO  */


document.addEventListener("DOMContentLoaded", function() {

	// Evento para asignar envío
	document.querySelectorAll(".asignarEnvio").forEach(button => {
		button.addEventListener("click", function() {
			let envioId = this.getAttribute("data-id");
			var datos = new FormData();
			var userId = $("#userId").val();
			datos.append("idEnvio", envioId);
			datos.append("userId", userId);

			$.ajax({
				url: globalPath + "/envios/asignar",
				method: "POST",
				data: datos,
				chache: false,
				contentType: false,
				processData: false,
				dataType: "json",
				success: function(respuesta) {
					var response = JSON.stringify(respuesta, null, '\t');
					var data = JSON.parse(response);
					if (data.status) {
						location.reload();
					} else {
						Swal.fire({
							icon: "error",
							text: "No se pudo asigar el envio al usuario.",

						})

					}
				}
			})

		});
	});

	// Evento para cambiar estado del envío
	document.querySelectorAll(".estadoEnvio").forEach(select => {
		select.addEventListener("change", function() {
			let envioId = this.getAttribute("data-id");
			let nuevoEstado = this.value;
			var datos = new FormData();

			datos.append("envioId", envioId);
			datos.append("nuevoEstado", nuevoEstado);



			$.ajax({
				url: globalPath + "/envios/cambiarEstado",
				method: "POST",
				data: datos,
				chache: false,
				contentType: false,
				processData: false,
				dataType: "json",
				success: function(respuesta) {
					var response = JSON.stringify(respuesta, null, '\t');
					var data = JSON.parse(response);
					if (data.status) {
						location.reload();
					} else {
						Swal.fire({
							icon: "error",
							text: "No se pudo asigar el envio al usuario.",

						})

					}
				}
			})

			
		});
	});

});


function generarEtiqueta() {
	let nombreCliente = "Juan Pérez";
	let direccionCliente = "Calle Falsa 123, Montevideo";
	let trackingNumber = "9400111899223663644123";

	//document.getElementById("nombreCliente").innerText = nombreCliente;
	//document.getElementById("direccionCliente").innerText = direccionCliente;
	//document.getElementById("codigoTracking").innerText = trackingNumber;

	// Mostrar el modal
	$("#modalEtiqueta").modal("show");

	// Esperar hasta que el modal esté visible para generar el código de barras
	$("#modalEtiqueta").on("shown.bs.modal", function() {
		JsBarcode("#barcode", trackingNumber, {
			format: "CODE128",
			displayValue: true,
			lineColor: "#000",
			width: 2,
			height: 50
		});
	});
}

function guardarEtiquetaEnBD(trackingNumber) {
	$.ajax({
		url: "/api/envios/guardarEtiqueta",
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify({ codigoSeguimiento: trackingNumber }),
		success: function(response) {
			console.log("Etiqueta guardada con éxito");
		},
		error: function(error) {
			console.log("Error al guardar la etiqueta", error);
		}
	});
}

// Función para imprimir la etiqueta
function imprimirEtiqueta() {
	var modalContent = document.querySelector("#modalEtiqueta .modal-content").innerHTML;
	var ventana = window.open("", "_blank");
	ventana.document.write('<html><head><title>Etiqueta de Envío</title></head><body>');
	ventana.document.write(modalContent);
	ventana.document.write('</body></html>');
	ventana.document.close();
	ventana.print();
}

/*$(document).ready(function() {
	let ventaService = new VentaService("/api/ventas/detalle", "/api/clientes/direccion");

	// Mostrar datos de la venta en el modal
	$(".verVenta").click(function(event) {
		event.preventDefault();
		let idVenta = $(this).data("idventa");

		ventaService.obtenerDatosVenta(idVenta, function(venta) {
			if (venta) {
				let contenido = `
					<tr><th>ID Venta:</th><td>${venta.idVenta}</td></tr>
					<tr><th>Cliente:</th><td>${venta.cliente}</td></tr>
					<tr><th>Monto Total:</th><td>${venta.montoTotal}</td></tr>
					<tr><th>Fecha:</th><td>${venta.fecha}</td></tr>
				`;
				$("#ventaDetalles").html(contenido);
				$("#modalVenta").modal("show");
			} else {
				Swal.fire({
					icon: "error",
					text: "No se encontraron datos de la venta.",

				})
				//alert("No se encontraron datos de la venta.");
			}
		});
	});

	// Mostrar dirección del cliente en el modal
	$(".verDireccion").click(function(event) {
		event.preventDefault();
		let idCliente = $(this).data("idcliente");

		ventaService.obtenerDireccionCliente(idCliente, function(direccion) {
			if (direccion) {
				let contenido = `
					<tr><th>Dirección:</th><td>${direccion.direccion}</td></tr>
					<tr><th>Ciudad:</th><td>${direccion.ciudad}</td></tr>
					<tr><th>Departamento:</th><td>${direccion.departamento}</td></tr>
					<tr><th>Teléfono:</th><td>${direccion.telefono}</td></tr>
				`;
				$("#direccionDetalles").html(contenido);
				$("#modalDireccion").modal("show");
			} else {
				alert("No se encontró la dirección del cliente.");
			}
		});
	});
});*/

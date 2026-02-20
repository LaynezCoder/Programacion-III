
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

public class PaintFX extends Application {

    // pilas para undo y redo
    private final Deque<BufferedImage> pilaUndo = new ArrayDeque<>();
    private final Deque<BufferedImage> pilaRedo = new ArrayDeque<>();

    private Canvas lienzo;
    private GraphicsContext lapiz;
    private Label info;

    // como va a dibujar
    private Color color = Color.BLACK;
    private double grosor = 4;

    private boolean dibujando = false;

    @Override
    public void start(Stage stage) {

        lienzo = new Canvas(720, 420);
        lapiz = lienzo.getGraphicsContext2D();

        info = new Label();

        // botones base
        Button bDeshacer = new Button("Deshacer");
        Button bRehacer = new Button("Rehacer");
        Button bBorrar = new Button("Borrar");
        Button bReset = new Button("Reset");

        bDeshacer.setOnAction(e -> deshacer());
        bRehacer.setOnAction(e -> rehacer());

        // borra el lienzo pero se puede deshacer
        bBorrar.setOnAction(e -> {
            guardarEstado();
            pilaRedo.clear();
            limpiarLienzo();
            refrescarInfo();
        });

        // reset = empezar de cero
        bReset.setOnAction(e -> {
            pilaUndo.clear();
            pilaRedo.clear();
            limpiarLienzo();
            guardarEstado(); // estado base limpio
            refrescarInfo();
        });

        // colores
        Button cNegro = new Button("Negro");
        Button cRojo = new Button("Rojo");
        Button cAzul = new Button("Azul");
        Button cVerde = new Button("Verde");

        cNegro.setOnAction(e -> {
            color = Color.BLACK;
            aplicarLapiz();
        });
        cRojo.setOnAction(e -> {
            color = Color.RED;
            aplicarLapiz();
        });
        cAzul.setOnAction(e -> {
            color = Color.DODGERBLUE;
            aplicarLapiz();
        });
        cVerde.setOnAction(e -> {
            color = Color.FORESTGREEN;
            aplicarLapiz();
        });

        // grosor rapido
        Button gDelgado = new Button("Delgado");
        Button gMedio = new Button("Medio");
        Button gGrueso = new Button("Grueso");

        gDelgado.setOnAction(e -> {
            grosor = 2;
            aplicarLapiz();
        });
        gMedio.setOnAction(e -> {
            grosor = 4;
            aplicarLapiz();
        });
        gGrueso.setOnAction(e -> {
            grosor = 8;
            aplicarLapiz();
        });

        HBox fila1 = new HBox(10, bDeshacer, bRehacer, bBorrar, bReset);
        HBox fila2 = new HBox(10,
                new Label("Color:"), cNegro, cRojo, cAzul, cVerde,
                new Label("Grosor:"), gDelgado, gMedio, gGrueso
        );

        VBox root = new VBox(10, fila1, fila2, lienzo, info);
        root.setPadding(new Insets(12));

        // arranque
        limpiarLienzo();
        aplicarLapiz();
        guardarEstado();  // estado inicial
        refrescarInfo();

        // ===== dibujo con mouse =====
        // inicio del trazo
        lienzo.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            dibujando = true;
            lapiz.beginPath();
            lapiz.moveTo(e.getX(), e.getY());
            lapiz.stroke();
        });

        // mientras arrastro
        lienzo.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (!dibujando) {
                return;
            }
            lapiz.lineTo(e.getX(), e.getY());
            lapiz.stroke();
        });

        // suelto el mouse y guardo el paso
        lienzo.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (!dibujando) {
                return;
            }
            dibujando = false;

            guardarEstado();
            pilaRedo.clear(); // si dibujo algo nuevo, redo ya no sirve
            refrescarInfo();
        });

        stage.setTitle("Dibujo Undo/Redo (Pilas) - JavaFX");
        stage.setScene(new Scene(root, 880, 560));
        stage.show();
    }

    // ===== undo =====
    private void deshacer() {
        // dejamos 1 estado minimo para no quedarnos sin nada
        if (pilaUndo.size() <= 1) {
            return;
        }

        pilaRedo.push(pilaUndo.pop());   // el actual se va a redo
        pintarImagen(pilaUndo.peek());   // muestro el anterior
        refrescarInfo();
    }

    // ===== redo =====
    private void rehacer() {
        if (pilaRedo.isEmpty()) {
            return;
        }

        BufferedImage estado = pilaRedo.pop();
        pilaUndo.push(estado);
        pintarImagen(estado);
        refrescarInfo();
    }

    // guardo una "foto" del lienzo en la pila undo
    private void guardarEstado() {
        WritableImage wi = lienzo.snapshot(new SnapshotParameters(), null);
        BufferedImage img = SwingFXUtils.fromFXImage(wi, null);
        pilaUndo.push(img);
    }

    // pongo una imagen guardada en el lienzo
    private void pintarImagen(BufferedImage img) {
        limpiarLienzo();
        lapiz.drawImage(SwingFXUtils.toFXImage(img, null), 0, 0);
        aplicarLapiz(); // para seguir con el color y grosor actual
    }

    // deja el lienzo en blanco
    private void limpiarLienzo() {
        lapiz.setFill(Color.WHITE);
        lapiz.fillRect(0, 0, lienzo.getWidth(), lienzo.getHeight());
    }

    // aplica el color y grosor al lapiz
    private void aplicarLapiz() {
        lapiz.setStroke(color);
        lapiz.setLineWidth(grosor);
    }

    // solo muestro contadores
    private void refrescarInfo() {
        info.setText("Undo: " + pilaUndo.size() + " | Redo: " + pilaRedo.size());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

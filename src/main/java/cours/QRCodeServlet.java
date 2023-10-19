package cours;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

@WebServlet(name = "QRCodeServlet", urlPatterns = {"/codebarre"})

public class QRCodeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Récup du paramètre POST contenant le code à encoder 
        String code = request.getParameter("codebarre");
        String lib = request.getParameter("libelle");
        String phrase = "Le code du formulaire est"   + code +". Le libellé est " + lib +".";

        // Vérif que le code-barres contient exactement 12 chiffres
        if (code == null || code.length() != 12 || !code.matches("[0-9]+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le code-barres doit contenir exactement 12 chiffres.");
            return;
        }

        // Création de l'objet
        DataMatrixBean bean = new DataMatrixBean();

        // Résolution de l'image
        final int dpi = 400;

        try {
            // Configuration du fournisseur de canevas bitmap
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // Génération du code-barre
            bean.generateBarcode(canvas, phrase);

            // Fermeture du canvas
            canvas.finish();

            // Enregistrement de l'image en tant que réponse au client
            response.setContentType("image/png");
            ImageIO.write(canvas.getBufferedImage(), "png", response.getOutputStream());

            System.out.println("Code-barre généré avec succès et renvoyé au client.");
            System.out.println(phrase);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la génération du code-barre ou de l'écriture de l'image.");
        }
    }
}
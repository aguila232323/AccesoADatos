import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;


public class JAXBUtils {
    public static void marshal(Estudiante estudiante, File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Estudiante.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(estudiante, file);
    }

    public static Estudiante unmarshal(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Estudiante.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Estudiante) unmarshaller.unmarshal(file);
    }
}


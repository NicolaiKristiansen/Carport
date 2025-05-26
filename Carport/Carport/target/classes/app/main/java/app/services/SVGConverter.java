package app.services;

import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

import java.io.*;

public class SVGConverter {
    public static ByteArrayOutputStream convertSvgToPng(String svgContent) throws Exception {
        PNGTranscoder transcoder = new PNGTranscoder();

        TranscoderInput input = new TranscoderInput(new StringReader(svgContent));
        ByteArrayOutputStream pngStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(pngStream);

        transcoder.transcode(input, output);
        pngStream.flush();
        return pngStream;
    }
}

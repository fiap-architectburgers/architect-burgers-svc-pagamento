package com.example.gomesrodris.archburgers.adapters.presenters;//import static org.junit.jupiter.api.Assertions.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QrCodePresenterTest {

    @Test
//    @Disabled
    void renderQrCode() throws IOException {
        /*
        This unit test requires a human for validation :-|
        Better to keep disabled by default
         */
        QrCodePresenter qrCodePresenter = new QrCodePresenter();

        var image = qrCodePresenter.renderQrCode("00020101021243650016COM.MERCADOLIBRE020130636787e9685-7de5-43f1-b09a-6d70f6f6c1e45204000053039865802BR5909Test Test6009SAO PAULO62070503***63043962");

        var output = Paths.get("/tmp/qrcode-test.png");
        Files.write(output, image);
        System.out.println("::::: Output saved to " + output);
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "very long text exceeding encoding capacity"})
    public void testRenderQrCode_WriterException(String invalidText) throws WriterException {

        QrCodePresenter qrCodePresenter = new QrCodePresenter();
        assertThrows(RuntimeException.class, () -> qrCodePresenter.renderQrCode(""));
    }


}
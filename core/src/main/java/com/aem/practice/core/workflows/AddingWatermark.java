package com.aem.practice.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

import javax.imageio.ImageIO;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {
                "process.label" + "= Adding watermark at assets"
        }
)
public class AddingWatermark implements WorkflowProcess {
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
        ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);
        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        try{
            Resource resource = resolver.getResource(payloadPath);
            Asset asset = resource.adaptTo(Asset.class);
            processAsset(asset, resolver);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void processAsset(Asset asset, ResourceResolver resolver) throws IOException, RepositoryException {
        String mimeType = asset.getMimeType();
        switch(mimeType) {
            case "image/jpeg":
            case "image/png":
            case "image/gif":
            case "image/bmp":
            case "image/tiff":
            case "image/svg+xml":
            case "image/webp":
                addWatermarkToImage(asset, mimeType);
                break;
            default:
                throw new IllegalArgumentException("Unsupported file type: "+mimeType);
        }
    }
    private void addWatermarkToImage(Asset asset, String mimeType) throws IOException, RepositoryException  {
        try{
            InputStream original = asset.getOriginal().getStream();
            BufferedImage image = ImageIO.read(original);
            if (image == null) {
                throw new IOException("Unable to decode image stream");
            }
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
            g2d.setComposite(alpha);
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            g2d.drawString("Nagarjuna",50,50);
            g2d.dispose();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, getFormatNameFromMimeType(mimeType), byteArrayOutputStream);
            updateAssetWithData(asset, new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String getFormatNameFromMimeType(String mimeType) {
        switch (mimeType) {
            case "image/jpeg":
                return "jpeg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            case "image/bmp":
                return "bmp";
            case "image/tiff":
                return "tiff";
            case "image/svg+xml":
                return "svg";
            case "image/webp":
                return "webp";
            default:
                return "jpeg";
        }
    }
    private void addWatermarkToOfficeDocument(Asset asset, String mimeType) throws IOException {
        if (mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            try (InputStream input = asset.getOriginal().getStream();
                 XWPFDocument doc = new XWPFDocument(input)) {

                XWPFParagraph p = doc.createParagraph();
                XWPFRun run = p.createRun();
                run.setText("VamsiKrishna");
                run.setColor("FF0000");
                run.setBold(true);
                run.setFontSize(50);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                doc.write(baos);
                updateAssetWithData(asset, new ByteArrayInputStream(baos.toByteArray()));
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void updateAssetWithData(Asset asset, InputStream dataStream) throws RepositoryException {
        Node jcrContent = asset.adaptTo(Node.class).getNode("jcr:content");
        if(jcrContent.hasNode("renditions/original")) {
            Node originalNode = jcrContent.getNode("renditions/original");
            Node jcrData = originalNode.getNode("jcr:content");
            jcrData.setProperty("jcr:data", dataStream);
            jcrData.getSession().save();
        }
    }
}

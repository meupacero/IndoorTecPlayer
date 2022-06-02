package indoortect.com.api.requests;

import android.os.Environment;

import com.google.firebase.storage.StorageReference;

import java.io.File;

import indoortec.com.entity.ApiStorageItem;
import indoortec.com.observer.Observer;
import indoortect.com.api.IndoorTecApi;

public class StorageRequest {
    private final StorageReference storageReference;
    private final static String TAG = StorageRequest.class.getName();

    public StorageRequest(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    public void download(ApiStorageItem itemDownload, Observer<Boolean> sucesso,Observer<Exception> exceptionObserver) {
        File rootFile = new File(Environment.getExternalStorageDirectory(),"indoortec");

        if (!rootFile.exists()) {
            rootFile.mkdir();
            rootFile.mkdirs();
        }

        rootFile = new File(rootFile,"pendencias");

        if (!rootFile.exists()) {
            rootFile.mkdir();
            rootFile.mkdirs();
        }

        String storage = itemDownload.getStorage();
        File midiaFile = new File(rootFile,storage);

        if (isaNull(storage)){
            exceptionObserver.observer(new Exception("ITEM INCOMPLETO : " + storage + " PULANDO DOWNLOAD"));
            return;
        }

        if (midiaFile.exists() && midiaFile.length() == Integer.parseInt(itemDownload.getSize())) {
            sucesso.observer(true);
        } else if (IndoorTecApi.nao_existe.contains(storage)){
            exceptionObserver.observer(new Exception("ESTA MIDIA NÃO EXISTE NO STORAGE"));
        }else {
            storageReference.getFile(midiaFile).addOnSuccessListener(taskSnapshot -> {
                sucesso.observer(true);
            }).addOnFailureListener(e -> {
                e.printStackTrace();

                if (e.getMessage() != null && e.getMessage().contains("Object does not exist at location")){
                    IndoorTecApi.nao_existe.add(storage);
                    sucesso.observer(false);
                }

                exceptionObserver.observer(new Exception("ERRO AO BAIXAR MIDIA : "+e.getMessage()));
            });
        }
    }

    private boolean isaNull(String value) {
        return value == null || value.isEmpty() || value.equals("null");
    }
}

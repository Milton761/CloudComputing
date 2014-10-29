package org.mcf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
//import java.io.FileNotFoundException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class ListDirectoryContents {
  public static void main(String[] args) throws IOException, URISyntaxException
  {
//    System.out.println(args[0]);

//recibiento path principal como argumento

    URI p = new URI(args[0]);

//creando el sistema de archivos:
    URI fileSystem = new URI(p.getScheme(),
        p.getUserInfo(), p.getHost(), p.getPort(),
        null, null,null);



//obtener la configuracion de la instancia:
    Configuration configuration = new Configuration();

//obtener la instancia del sistema de archivos:
    FileSystem hdfs = FileSystem.get(fileSystem, configuration);

//enlistar los directorios
     listFiles(new Path(p),hdfs);

  }

//para enlistar los directorios
  public static void listFiles(Path dir, FileSystem hdfs) throws IOException, URISyntaxException {

//vector de contenido de un directorio "dir":

    FileStatus[] fileStatus = hdfs.listStatus(dir);

//recorremos todo el vector
    for( FileStatus stat : fileStatus)
    {
//imprimimos su ruta
      System.out.println(stat.getPath());

//si el elemento del vector es una carpeta, llamamos recursivamente a la funcion con el nombre actual de la carpeta
      if(stat.isDirectory())
      {
        listFiles(stat.getPath(),hdfs);
      }
    }
  }
}

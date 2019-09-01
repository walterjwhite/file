package com.walterjwhite.file.modules.tar;

public class TreeCopierUtil {
  /*
  public static void copy(final File source, final File target){
      List<String> result = new ArrayList<String>();
      InputStream inputStream = new FileInputStream(tarFile);
      TarArchiveInputStream in = new TarArchiveInputStream(inputStream);
      TarArchiveEntry entry = in.getNextTarEntry();
      while (entry != null) {
          if (entry.isDirectory()) {
              entry = in.getNextTarEntry();
              continue;
          }
          File curfile = new File(directory, entry.getName());
          File parent = curfile.getParentFile();
          if (!parent.exists()) {
              parent.mkdirs();
          }
          OutputStream out = new FileOutputStream(curfile);
          IOUtils.copy(in, out);
          out.close();
          result.add(entry.getName());
          entry = in.getNextTarEntry();
      }
      in.close();
      return result;
  }
  */
}

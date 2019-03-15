public class ExorTitkosító {
    
    public ExorTitkosító(String kulcsSzöveg,
            java.io.InputStream bejövőCsatorna,
            java.io.OutputStream kimenőCsatorna)
            throws java.io.IOException {
        
        byte [] kulcs = kulcsSzöveg.getBytes();
        byte [] buffer = new byte[256];
        int kulcsIndex = 0;
        int olvasottBájtok = 0;

        while((olvasottBájtok =
                bejövőCsatorna.read(buffer)) != -1) {
            
            for(int i=0; i<olvasottBájtok; ++i) {
                
                buffer[i] = (byte)(buffer[i] ^ kulcs[kulcsIndex]);
                kulcsIndex = (kulcsIndex+1) % kulcs.length;
                
            }
            
            kimenőCsatorna.write(buffer, 0, olvasottBájtok);
            
        }
        
    }
    
    public static void main(String[] args) {
        
        try {
            
            new ExorTitkosító(args[0], System.in, System.out);
            
        } catch(java.io.IOException e) {
            
            e.printStackTrace();
            
        }
        
    }
    
}

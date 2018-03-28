package unalcol.reflect.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.w3c.dom.Element;

public class PlugInManager extends PlugInLoader{
	protected String plugin_path;
	public static final String jar="jar";
	
	protected String repository_url;
	
	public PlugInManager( String plugin_path ){ this(plugin_path, ""); }
	
	public PlugInManager( String plugin_path, String repository_url ){
		this.repository_url = repository_url;
		try{ this.plugin_path = new URL(plugin_path).getPath(); } catch (MalformedURLException e) { this.plugin_path = plugin_path; }
		loadPluginsForFolder(new File(plugin_path));
	}
	
	protected boolean download( String plugin ){
		try{
			PlugInManifest manifest = new PlugInManifest(repository_url);
			Set<String> plugins = manifest.plugins();
			for( String pl : plugins ){
				String jarFileURL = repository_url+manifest.info(pl,jar);
				PlugInManifest jarManifest = new PlugInManifest(jarFileURL);
				if(jarManifest.contains(plugin)){
					install(jarFileURL);
					return true;
				}
			}
		}catch(IOException e){}	
		return false;
	}
	
 	public Object load(String plugin) throws PlugInException{
		if( loader.get(plugin) == null ){ download(plugin); }
		return super.load(plugin); 
 	}

 	public PlugIn load(Element plugin) throws PlugInException{
		if( loader.get(plugin.getTagName()) == null ){ download(plugin.getTagName()); }
		return super.load(plugin); 
 	} 	

	
	public void loadPluginsForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) loadPluginsForFolder(fileEntry);
	        else{
	        	try{ add(fileEntry.toURI().toURL(), PlugInManager.class.getClassLoader()); }
	        	catch (IOException e){ e.printStackTrace(); }
	        }
	    }
	}
	
	public void install( String url ) throws IOException{
		install( new URL(url) );
	}
	
	public void install( URL url ) throws IOException{
		String path = url.getPath(); 
		if( path.endsWith("/") ){
			// https://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file
		}else{
			String container = path.substring(path.lastIndexOf('/')+1, path.length()); 
			InputStream is = url.openStream();
			FileOutputStream os = new FileOutputStream(plugin_path+container);
			byte[] buffer = new byte[100000];
			int k = is.read(buffer);
			while(k>0){
				os.write(buffer,0,k);
				k = is.read(buffer);
			}
			os.close();
			is.close();
			add(new File(plugin_path+container).toURI().toURL(), PlugInManager.class.getClassLoader());
		}
	}

	public static void main(String[] args){
		PlugInManager manager = new PlugInManager("plugins");
		try {
			Set<String> plugins = manager.plugins();
			for( String s:plugins ){
				Object c = manager.load(s);
				System.out.println(c);
			}	
		} catch (PlugInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

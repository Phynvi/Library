package util.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * A ConfigSection which is based on a particular file. This class contains methods for reloading
 * and saving a ConfigSection.
 * 
 * @author Dirk Jamieson
 */
public class FileConfig extends ConfigSection {

	private static final Gson gson;
	
	static {
		gson = new GsonBuilder().setPrettyPrinting().create();
		/*
		 * registerTypeHierarchyAdapter(Map.class, new JsonSerializer<Map<?, ?>>() {
			@Override
			public JsonElement serialize(Map<?, ?> src, Type typeOfSrc, JsonSerializationContext context) {
				if (src == null || src.isEmpty())
					return null;
				JsonObject obj = new JsonObject();
				for(Map.Entry<?, ?> entry : src.entrySet()) {
					obj.add(entry.getKey().toString(), context.serialize(entry.getValue()));
				}
				return obj;
			}
		})
		 */
	}
	
	/** The file we write to */
	private final File file;

	/**
	 * Creates a new FileConfig based on the given file. This method does not load the data from the
	 * file. Instead you should call FileConfig.reload() if you wish to view previously stored values.
	 * 
	 * @param file
	 *            the file to base this config on.
	 */
	public FileConfig(File file) {
		this.file = file;
	}

	@SuppressWarnings("unchecked")
	public void load(ConfigType type) {
		try {
			FileInputStream in = new FileInputStream(file);
			Map<String, Object> data = null;
			switch (type) {
			case BINARY:
				break;
			case JSON:
				try(FileReader reader = new FileReader(file)) {
					data = gson.fromJson(reader, Map.class);
				}
				break;
			case TXT:
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String string;
				int count = 0;
				while ((string = reader.readLine()) != null)
					this.map.put(count++, string);
				break;
			case YAML:
				DumperOptions options = new DumperOptions();
				options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
				Yaml parser = new Yaml(options);
				data = (Map<String, Object>) parser.load(in);
				this.map.putAll(data);
				break;
			default:
				break;
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			// File does not exist, therefore it has no data to load.
		}
	}

	/**
	 * Writes this FileConfig to disk. This creates the file if it does not exist, including parent
	 * folders.
	 * 
	 * @throws IOException
	 *             if an IO error occured.
	 */
	public void save(ConfigType type) {
		try {
			if (!file.exists()) {
				if (file.getParentFile() != null)
					file.getParentFile().mkdirs();
				file.createNewFile();
			}
			PrintStream ps = new PrintStream(file);
			String out = null;
			switch (type) {
			case BINARY:
				break;
			case JSON:
				gson.toJson(map, ps);
				ps.flush();
				break;
			case TXT:
				break;
			case YAML:
				DumperOptions options = new DumperOptions();
				options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
				Yaml parser = new Yaml(options);
				out = parser.dump(map);
				break;
			default:
				break;
			}
			if(out != null)
				ps.print(out);
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			// File does not exist, therefore it has no data to load.
		}
	}
}
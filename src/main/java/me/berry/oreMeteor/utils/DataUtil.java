package me.berry.oreMeteor.utils;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataUtil {
	public static AtomicBoolean writing = new AtomicBoolean(false);

	public HashMap<?, ?> initVars(File f) {
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			return new HashMap<>();
		} else return (HashMap<?, ?>) load(f);
	}

	public Object load(File f) {
		try {
			ObjectInputStream data = new ObjectInputStream(new FileInputStream(f));
			Object result = data.readObject();
			data.close();
			return result;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}


	public boolean saveDataFunction(File f, HashMap<?, ?> hashMap) {
		if(!writing.get()) {
			writing.getAndSet(true);
			if(f.exists()) try {
				ObjectOutputStream data = new ObjectOutputStream(new FileOutputStream(f));
				data.writeObject(hashMap);
				data.flush();
				data.close();

				return true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
		} else return false;

		return true;
	}
}

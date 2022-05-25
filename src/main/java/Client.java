import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws IOException {
		int port = 8099;
		String host = "netology.homework";

		try (Socket clientSocket = new Socket(host, port);
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			out.println("devops");
			String responce = in.readLine();
			String responceJson = niceOutputToJson(responce);
			System.out.print(responceJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String niceOutputToJson(String source) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(source);
		String jsonString = gson.toJson(je);
		return jsonString;
	}
}

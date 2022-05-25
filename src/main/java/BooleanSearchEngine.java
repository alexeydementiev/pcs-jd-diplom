import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BooleanSearchEngine implements SearchEngine {

	public final Map<String, List<PageEntry>> index = new HashMap<>();

	public BooleanSearchEngine(File pdfsDir) throws IOException {

		try (Stream<Path> paths = Files.walk(Paths.get(pdfsDir.getPath()))) {
			paths
					.filter(Files::isRegularFile)
					.forEach(f -> {
						try (PdfDocument doc = new PdfDocument(new PdfReader(f.toFile()))) {
							indexingPdfFile(doc, String.valueOf(f.getFileName()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}
	}

	private void indexingPdfFile(PdfDocument doc, String pdfName) {
		for (int i = 1; i <= doc.getNumberOfPages(); i++) {
			PdfPage page = doc.getPage(i);
			String text = PdfTextExtractor.getTextFromPage(page);
			String[] words = text.split("\\P{IsAlphabetic}+");
			Map<String, Integer> freqs = new HashMap<>();
			for (var word : words) {
				if (word.isEmpty()) {
					continue;
				}
				freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
			}

			for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
				List<PageEntry> pageEntryList = new ArrayList<>();
				if (index.containsKey(entry.getKey())) {
					pageEntryList = index.get(entry.getKey());
				} else {
					pageEntryList = new ArrayList<>();
				}
				PageEntry pageEntry = new PageEntry(pdfName, i, entry.getValue());
				pageEntryList.add(pageEntry);
				Collections.sort(pageEntryList);
				index.put(entry.getKey(), pageEntryList);
			}
		}
	}

	@Override
	public List<PageEntry> search(String word) {
		if (index.containsKey(word)) {
			return index.get(word);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public String listToJson(List<PageEntry> list) {
		Type listType = new TypeToken<List<PageEntry>>() {
		}.getType();
		Gson gson = new GsonBuilder().create();
		String jsonString = gson.toJson(list, listType);
		return jsonString;
	}
}

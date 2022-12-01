package nl.enjarai.cicada;

import net.fabricmc.api.ModInitializer;
import nl.enjarai.cicada.util.ProperLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cicada implements ModInitializer {
	public static final String MODID = "cicada";
	public static final Logger LOGGER = ProperLogger.getLogger(MODID);

	@Override
	public void onInitialize() {
		LOGGER.info("");
	}
}

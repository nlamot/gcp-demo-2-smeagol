package be.ae.googleclouddemo.smeagol.quote;

import java.util.Random;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import be.ae.googleclouddemo.smeagol.SmeagolApplication.MessageOutboundGateway;


@Service
public class QuoteService {

	private final QuoteRepository repository;

	private final MessageOutboundGateway messageGateway;

	@Value("${spring.cloud.gcp.sql.database-name}")
	private String speaker;

	public QuoteService(QuoteRepository repository, MessageOutboundGateway messageGateway) {
		this.repository = repository;
		this.messageGateway = messageGateway;
	}

	public SmeagolQuote randomQuote() {
		Random random = new Random();
		String quote = repository.findOne(random.nextInt((int)repository.count()) + 1).getText();
		messageGateway.sendToPubsub(quote);
		Logger.getGlobal().info(speaker + ": " + quote);
		return new SmeagolQuote(quote, speaker);
	}
}

package ratpack.example.books

import groovy.transform.CompileStatic
import org.pac4j.core.profile.UserProfile
import ratpack.exec.Promise
import ratpack.groovy.template.MarkupTemplate
import ratpack.handling.Context
import ratpack.pac4j.internal.Pac4jSessionKeys
import ratpack.render.RenderableDecoratorSupport
import ratpack.session.Session
import ratpack.session.SessionData

@CompileStatic
class MarkupTemplateRenderableDecorator extends RenderableDecoratorSupport<MarkupTemplate> {
	@Override
	Promise<MarkupTemplate> decorate(Context context, MarkupTemplate template) {
		return context.get(Session).data.map { SessionData d ->
			d.get(Pac4jSessionKeys.USER_PROFILE_SESSION_KEY).orElse(null)
		}.map { UserProfile user ->
			def username = user?.attributes?.username
			template.model.putAll([username: username] as Map)
			new MarkupTemplate(template.name,
					template.contentType,
					template.model)}
	}

}

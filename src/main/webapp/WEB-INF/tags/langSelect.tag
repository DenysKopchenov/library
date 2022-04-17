<%@ tag import="com.dkop.library.utils.LocalizationUtil" %>
<%
        String language = (String) session.getAttribute("language");
        StringBuilder builder = new StringBuilder();
        builder.append("<div class=\"btn-group\">")
                .append("<button type=\"button\" class=\"btn dropdown-toggle\" data-toggle=\"dropdown\" aria-haspopup=\"true\"aria-expanded=\"false\">");
        if (language.equals("en")) {
            builder.append(LocalizationUtil.localizationBundle.getString("language"));
        } else {
            builder.append(LocalizationUtil.localizationBundle.getString("language"));
        }
        builder.append("</button>")
                .append("<div class=\"dropdown-menu\">")
                .append("<a class=\"dropdown-item\" href=\"?lang=en\">")
                .append(LocalizationUtil.localizationBundle.getString("language.en"))
                .append("</a>")
                .append("<a class=\"dropdown-item\" href=\"?lang=uk\">")
                .append(LocalizationUtil.localizationBundle.getString("language.ua"))
                .append("</a>")
                .append("</div>")
                .append("</div>");
                out.println(builder);
%>
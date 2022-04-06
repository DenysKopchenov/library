package com.dkop.library.controller.command;

import com.dkop.library.entity.Book;
import com.dkop.library.services.PaginationService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CatalogCommand implements Command {
    private final PaginationService paginationService = PaginationService.getInstance();

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("sort", request.getParameter("sort"));
        String sortBy = request.getParameter("sort");
        int perPage = 5;
        if (StringUtils.isNumeric(request.getParameter("perPage"))) {
            perPage = Integer.parseInt(request.getParameter("perPage"));
        }
        int page = 1;
        if (StringUtils.isNumeric(request.getParameter("page"))) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Book> catalog;
        if (sortBy != null) {
            switch (sortBy) {
                case "author":
                    request.setAttribute("sortBy", "By Author");
                    break;
                case "publisher":
                    request.setAttribute("sortBy", "By Publisher");
                    break;
                case "publishing_date":
                    request.setAttribute("sortBy", "By Publishing date");
                    break;
                default:
                    request.setAttribute("sortBy", "By Title");
            }
            catalog = paginationService.paginateBooks(sortBy, page, perPage);
        } else {
            catalog = paginationService.paginateBooks("title", page, perPage);
        }
        int numberOfPages = paginationService.countNumberOfPagesForBooks(perPage);
        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("perPage", perPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("catalog", catalog);

        return "/WEB-INF/catalog.jsp";
    }
}

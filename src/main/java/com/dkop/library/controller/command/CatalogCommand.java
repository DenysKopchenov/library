package com.dkop.library.controller.command;

import com.dkop.library.entity.Book;
import com.dkop.library.services.PaginationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.dkop.library.utils.Fields.SORT_BY;
import static com.dkop.library.utils.LocalizationUtil.localizationBundle;

public class CatalogCommand implements Command {

    private final PaginationService paginationService;

    public CatalogCommand(PaginationService paginationService) {
        this.paginationService = paginationService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("sort", request.getParameter("sort"));
        String sortBy = request.getParameter("sort");
        int perPage = paginationService.getRecordsPerPage(request);
        int page = paginationService.getPageNumber(request);
        List<Book> catalog;
        if (sortBy != null) {
            switch (sortBy) {
                case "author":
                    request.setAttribute(SORT_BY, localizationBundle.getString("catalog.sort.author"));
                    break;
                case "publisher":
                    request.setAttribute(SORT_BY, localizationBundle.getString("catalog.sort.publisher"));
                    break;
                case "publishing_date":
                    request.setAttribute(SORT_BY, localizationBundle.getString("catalog.sort.publishing.date"));
                    break;
                default:
                    request.setAttribute(SORT_BY, localizationBundle.getString("catalog.sort.title"));
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

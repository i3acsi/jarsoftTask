package ru.gasevsky.jarsoft.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.gasevsky.jarsoft.dto.BannerDto;
import ru.gasevsky.jarsoft.model.Banner;
import ru.gasevsky.jarsoft.model.Request;
import ru.gasevsky.jarsoft.repo.BannerDtoRepo;
import ru.gasevsky.jarsoft.repo.BannerRepo;
import ru.gasevsky.jarsoft.repo.CategoryRepo;
import ru.gasevsky.jarsoft.repo.RequestRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BidService {
    private final RequestRepo requestRepo;
    private final BannerRepo bannerRepo;
    private final CategoryRepo categoryRepo;

    //В ответ приложение возвращает текст баннера из указанной в запросе категории.
// При наличии нескольких баннеров с такой категорией должен выбираться баннер с самой высокой ценой. Если присутствует несколько баннеров с самой высокой ценой, 
// то среди них выбирается случайный. Если по данному запросу не нашлось ни одного баннера, то сервер должен вернуть HTTP status 204.
    public ResponseEntity<String> getBanner(String reqName) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        List<Banner> list = bannerRepo.findByCategory_ReqNameOrderByPriceDesc(reqName);
        HttpSession httpSession = request.getSession();
        final HashSet<Integer> watchedBanners = (HashSet<Integer>) httpSession.getAttribute("set");
        Banner banner = null;
        if (watchedBanners != null) {
            Optional<Banner> optBanner = list.stream().filter(b -> !watchedBanners.contains(b.getId())).findFirst();
            if (optBanner.isPresent()) {
                banner = optBanner.get();
                watchedBanners.add(banner.getId());
            }
        } else {
            Optional<Banner> optBanner = list.stream().findFirst();
            if (optBanner.isPresent()) {
                banner = optBanner.get();
                HashSet<Integer> set = new HashSet<>();
                set.add(banner.getId());
                httpSession.setAttribute("set", set);
            }
        }
        if (banner != null) {
            Request req = new Request();
            req.setBanner(banner);
            req.setUserAgent(request.getHeader("User-Agent"));
            req.setIpAddress(request.getRemoteAddr());
            requestRepo.save(req);
            return new ResponseEntity<String>(
                    banner.getContent(),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<String>("", HttpStatus.NO_CONTENT);
    }
}

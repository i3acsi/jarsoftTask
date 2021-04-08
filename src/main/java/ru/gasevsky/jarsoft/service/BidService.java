package ru.gasevsky.jarsoft.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gasevsky.jarsoft.model.Banner;
import ru.gasevsky.jarsoft.model.Request;
import ru.gasevsky.jarsoft.repo.BannerRepo;
import ru.gasevsky.jarsoft.repo.RequestRepo;
import ru.gasevsky.jarsoft.repo.SessionMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BidService {
    private final RequestRepo requestRepo;
    private final BannerRepo bannerRepo;
    private final SessionMap sessionMap;

    public ResponseEntity<String> getBanner(String reqName, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        List<Integer> watchedBanners = sessionMap.getWatchedBanners(ipAddress, userAgent);
        List<Banner> list = bannerRepo.findActiveBannersForReqName(reqName);
        Optional<Banner> optBanner = watchedBanners.size() == 0 ?
                list.stream().findFirst()
                : list.stream().filter(b -> !watchedBanners.contains(b.getId())).findFirst();
        if (optBanner.isPresent()) {
            Banner banner = optBanner.get();
            sessionMap.acceptBanner(ipAddress, userAgent, banner);
            Request req = new Request();
            req.setBanner(banner);
            req.setUserAgent(request.getHeader("User-Agent"));
            req.setIpAddress(request.getRemoteAddr());
            requestRepo.save(req);
            return new ResponseEntity<>(
                    banner.getContent(),
                    HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("", HttpStatus.valueOf(204));
    }
}

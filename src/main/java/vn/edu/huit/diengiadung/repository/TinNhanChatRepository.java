package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.TinNhanChat;

import java.util.List;
import java.util.Optional;

public interface TinNhanChatRepository extends JpaRepository<TinNhanChat, Long> {

    List<TinNhanChat> findTop100ByPhienChatIdOrderByIdDesc(Long phienChatId);

    Optional<TinNhanChat> findFirstByPhienChatIdOrderByIdDesc(Long phienChatId);

    long countByPhienChatId(Long phienChatId);
}

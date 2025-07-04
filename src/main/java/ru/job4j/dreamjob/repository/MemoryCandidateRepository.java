package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {
    private AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Александр", "Junior+ Java Developer",
                LocalDateTime.now(), true, 2, 0));
        save(new Candidate(0, "Борис", "Intern Java Developer",
                LocalDateTime.now(), true, 1, 0));
        save(new Candidate(0, "Елена", "Middle Java Developer",
                LocalDateTime.now(), true, 3, 0));
        save(new Candidate(0, "Николай", "Senior Java Developer",
                LocalDateTime.now(), true, 3, 0));
        save(new Candidate(0, "Владимир", "Middle+ Java Developer",
                LocalDateTime.now(), true, 2, 0));
        save(new Candidate(0, "Валентина", "Junior Java Developer",
                LocalDateTime.now(), true, 1, 0));

    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getName(),
                        candidate.getDescription(), candidate.getCreationDate(),
                        candidate.getVisible(), candidate.getCityId(), candidate.getFileId())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}

package com.diy.app;

import com.diy.framework.web.mvc.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class LectureRepository {

    private final Map<Long, Lecture> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    public LectureRepository(){
    }

//    private LectureRepository(){
//        if (Holder.instance != null) {
//            throw new RuntimeException("Reflection 으로 private 생성자를 호출할 수 없습니다.");
//        }
//    }
//
//    private static class Holder{
//        private static final LectureRepository instance = new LectureRepository();
//    }
//    public static LectureRepository getInstance() {
//        return Holder.instance;
//    }

    public Lecture save(Lecture lecture){
        long id = this.seq.incrementAndGet();
        Lecture newLecture = new Lecture(id, lecture.getName(), lecture.getPrice());
        this.store.put(id, newLecture);
        return newLecture;
    }

    public Optional<Lecture> update(Lecture lecture) {
        return Optional.ofNullable(this.store.computeIfPresent(lecture.getId(), (key, old) -> {
            return new Lecture(
                    lecture.getId(),
                    lecture.getName(),
                    lecture.getPrice()
            );
        }));
    }

    public Optional<Lecture> delete(Long id){
        return Optional.ofNullable(this.store.remove(id));
    }

    public Lecture findById(Long id){
        return this.store.get(id);
    }

    public Collection<Lecture> findAll(){
        return this.store.values();
    }


}

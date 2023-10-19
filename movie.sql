# 1. 영화 '퍼스트 맨'의 제작 연도, 영문 제목, 러닝 타임, 플롯을 출력하세요.
select ReleaseDateInKorea, Title, RunningTime, Plot from movie where KoreanTitle = '퍼스트 맨';

# 2. 2003년에 개봉한 영화의 한글 제목과 영문 제목을 출력하세요
select KoreanTitle, Title from movie where ReleaseYear=2003;


# 3. 영화 '글래디에이터'의 작곡가를 고르세요
select Name
from person
where PersonID IN (SELECT appear.PersonID
                  from appear
                  where RoleID IN (SELECT RoleID from role where RoleKorName = '작곡')
                    AND MovieID IN (select MovieID from movie where KoreanTitle = '글래디에이터'));


# 4. 영화 '매트릭스' 의 감독이 몇명인지 출력하세요

select COUNT(Name)
from person
where PersonID IN (SELECT appear.PersonID
                  from appear
                  where RoleID IN (SELECT RoleID from role where RoleKorName = '감독')
                    AND MovieID IN (select MovieID from movie where KoreanTitle = '매트릭스'));

# 5. 감독이 2명 이상인 영화를 출력하세요

select Title
from movie
where MovieID IN
      (select MovieID from appear where RoleID IN (SELECT RoleID from role where RoleKorName = '감독') group by MovieID HAVING COUNT(MovieID) >= 2);

# 6. '한스 짐머'가 참여한 영화 중 아카데미를 수상한 영화를 출력하세요

select Title
from movie
where MovieID IN (select MovieID
                  from appear
                  where PersonID IN (SELECT PersonID from person where KoreanName = '한스 짐머')
                    and AppearID IN (select AppearID
                                     from awardinvolve where WinningID IN (2)));






# 7. 감독이 '제임스 카메론'이고 '아놀드 슈워제네거'가 출연한 영화를 출력하세요

select Title
from movie
where MovieID IN (SELECT MovieID
                  from appear
                  where PersonID IN
                        (select PersonID from person where KoreanName IN ('제임스 카메론') OR KoreanName IN ('아놀드 슈워제네거')));

# 8. 상영시간이 100분 이상인 영화 중 레오나르도 디카프리오가 출연한 영화를 고르시오

select Title
from movie
where RunningTime >= 100
  AND MovieID IN
      (SELECT MovieID from appear where PersonID IN (SELECT PersonID from person where KoreanName IN ('레오나르도 디카프리오')));

# 9. 청소년 관람불가 등급의 영화 중 가장 많은 수익을 얻은 영화를 고르시오

Select Title from movie where GradeInKoreaID =4 ORDER BY (BoxOfficeWWGross + BoxOfficeUSGross) DESC LIMIT 1;


# 10. 1999년 이전에 제작된 영화의 수익 평균을 고르시오

select avg(BoxOfficeUSGross + BoxOfficeWWGross) AS 영화수익평균
from movie where ReleaseYear <=1999;

# 11. 가장 많은 제작비가 투입된 영화를 고르시오.

select Title
from movie
order by Budget desc limit 1;

# 12. 제작한 영화의 제작비 총합이 가장 높은 감독은 누구입니까?


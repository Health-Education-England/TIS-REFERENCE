import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {GradeDetailComponent} from "../../../../../../main/webapp/app/entities/grade/grade-detail.component";
import {GradeService} from "../../../../../../main/webapp/app/entities/grade/grade.service";
import {Grade} from "../../../../../../main/webapp/app/entities/grade/grade.model";

describe('Component Tests', () => {

	describe('Grade Management Detail Component', () => {
		let comp: GradeDetailComponent;
		let fixture: ComponentFixture<GradeDetailComponent>;
		let service: GradeService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [GradeDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					GradeService
				]
			}).overrideComponent(GradeDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(GradeDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(GradeService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Grade(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.grade).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});

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
import {CurriculumSubTypeDetailComponent} from "../../../../../../main/webapp/app/entities/curriculum-sub-type/curriculum-sub-type-detail.component";
import {CurriculumSubTypeService} from "../../../../../../main/webapp/app/entities/curriculum-sub-type/curriculum-sub-type.service";
import {CurriculumSubType} from "../../../../../../main/webapp/app/entities/curriculum-sub-type/curriculum-sub-type.model";

describe('Component Tests', () => {

	describe('CurriculumSubType Management Detail Component', () => {
		let comp: CurriculumSubTypeDetailComponent;
		let fixture: ComponentFixture<CurriculumSubTypeDetailComponent>;
		let service: CurriculumSubTypeService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [CurriculumSubTypeDetailComponent],
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
					CurriculumSubTypeService
				]
			}).overrideComponent(CurriculumSubTypeDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(CurriculumSubTypeDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(CurriculumSubTypeService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new CurriculumSubType(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.curriculumSubType).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});

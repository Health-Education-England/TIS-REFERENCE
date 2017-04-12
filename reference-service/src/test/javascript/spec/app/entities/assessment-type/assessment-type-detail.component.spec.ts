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
import {AssessmentTypeDetailComponent} from "../../../../../../main/webapp/app/entities/assessment-type/assessment-type-detail.component";
import {AssessmentTypeService} from "../../../../../../main/webapp/app/entities/assessment-type/assessment-type.service";
import {AssessmentType} from "../../../../../../main/webapp/app/entities/assessment-type/assessment-type.model";

describe('Component Tests', () => {

	describe('AssessmentType Management Detail Component', () => {
		let comp: AssessmentTypeDetailComponent;
		let fixture: ComponentFixture<AssessmentTypeDetailComponent>;
		let service: AssessmentTypeService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [AssessmentTypeDetailComponent],
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
					AssessmentTypeService
				]
			}).overrideComponent(AssessmentTypeDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(AssessmentTypeDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(AssessmentTypeService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new AssessmentType(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.assessmentType).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});

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
import {MedicalSchoolDetailComponent} from "../../../../../../main/webapp/app/entities/medical-school/medical-school-detail.component";
import {MedicalSchoolService} from "../../../../../../main/webapp/app/entities/medical-school/medical-school.service";
import {MedicalSchool} from "../../../../../../main/webapp/app/entities/medical-school/medical-school.model";

describe('Component Tests', () => {

	describe('MedicalSchool Management Detail Component', () => {
		let comp: MedicalSchoolDetailComponent;
		let fixture: ComponentFixture<MedicalSchoolDetailComponent>;
		let service: MedicalSchoolService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [MedicalSchoolDetailComponent],
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
					MedicalSchoolService
				]
			}).overrideComponent(MedicalSchoolDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(MedicalSchoolDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(MedicalSchoolService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new MedicalSchool(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.medicalSchool).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});

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
import {CollegeDetailComponent} from "../../../../../../main/webapp/app/entities/college/college-detail.component";
import {CollegeService} from "../../../../../../main/webapp/app/entities/college/college.service";
import {College} from "../../../../../../main/webapp/app/entities/college/college.model";

describe('Component Tests', () => {

	describe('College Management Detail Component', () => {
		let comp: CollegeDetailComponent;
		let fixture: ComponentFixture<CollegeDetailComponent>;
		let service: CollegeService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [CollegeDetailComponent],
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
					CollegeService
				]
			}).overrideComponent(CollegeDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(CollegeDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(CollegeService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new College(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.college).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
